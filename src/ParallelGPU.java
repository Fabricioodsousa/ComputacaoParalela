import org.jocl.*;

public class ParallelGPU {
    private static boolean initialized = false;
    private static cl_context context;
    private static cl_command_queue queue;
    private static cl_program program;
    private static cl_kernel kernel;
    private static cl_device_id device;

    private static final String kernelSource =
        "__kernel void countKernel(__global const char* text, __global const int* offsets,"
      + " __global const char* target, const int tLen, __global int* out) {"
      + "  int i = get_global_id(0);"
      + "  int start = offsets[i];"
      + "  int end = offsets[i+1];"
      + "  int len = end - start;"
      + "  if (len != tLen) { out[i] = 0; return; }"
      + "  for (int k = 0; k < tLen; k++) {"
      + "    char c1 = text[start + k]; char c2 = target[k];"
      + "    if (c1 >= 'A' && c1 <= 'Z') c1 += 32;"
      + "    if (c2 >= 'A' && c2 <= 'Z') c2 += 32;"
      + "    if (c1 != c2) { out[i] = 0; return; }"
      + "  } out[i] = 1; }";

    public static void initOnceIfNeeded() {
        if (initialized) return;
        CL.setExceptionsEnabled(true);
        cl_platform_id[] platforms = new cl_platform_id[1];
        CL.clGetPlatformIDs(1, platforms, null);
        cl_device_id[] devices = new cl_device_id[1];
        int err = CL.clGetDeviceIDs(platforms[0], CL.CL_DEVICE_TYPE_GPU, 1, devices, null);
        if (err != CL.CL_SUCCESS) {
            // tentar CPU
            CL.clGetDeviceIDs(platforms[0], CL.CL_DEVICE_TYPE_CPU, 1, devices, null);
        }
        device = devices[0];
        context = CL.clCreateContext(null, 1, new cl_device_id[]{device}, null, null, null);
        queue = CL.clCreateCommandQueue(context, device, 0, null);
        program = CL.clCreateProgramWithSource(context, 1, new String[]{ kernelSource }, null, null);
        CL.clBuildProgram(program, 0, null, null, null, null);
        kernel = CL.clCreateKernel(program, "countKernel", null);
        initialized = true;
    }

    public static void countGPUWarmup(String text, String target) {
        initOnceIfNeeded();
        countGPU(text, target);
    }

    public static int countGPU(String text, String target) {
        if (!initialized) initOnceIfNeeded();

        String[] words = text.split("\\W+");
        int n = words.length;
        int[] results = new int[n];

        StringBuilder sb = new StringBuilder();
        int[] offsets = new int[n + 1];
        int pos = 0;
        for (int i = 0; i < n; i++) { offsets[i] = pos; sb.append(words[i]); pos += words[i].length(); }
        offsets[n] = pos;

        byte[] textBuffer = sb.toString().getBytes();
        byte[] targetBuffer = target.toLowerCase().getBytes();

        cl_mem memText = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * textBuffer.length, Pointer.to(textBuffer), null);

        cl_mem memOffsets = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_int * offsets.length, Pointer.to(offsets), null);

        cl_mem memTarget = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY | CL.CL_MEM_COPY_HOST_PTR,
                Sizeof.cl_char * targetBuffer.length, Pointer.to(targetBuffer), null);

        cl_mem memOut = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY, Sizeof.cl_int * n, null, null);

        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(memText));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(memOffsets));
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(memTarget));
        CL.clSetKernelArg(kernel, 3, Sizeof.cl_int, Pointer.to(new int[]{ target.length() }));
        CL.clSetKernelArg(kernel, 4, Sizeof.cl_mem, Pointer.to(memOut));

        long[] global = new long[]{ n };
        CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, global, null, 0, null, null);

        CL.clEnqueueReadBuffer(queue, memOut, CL.CL_TRUE, 0, n * 4, Pointer.to(results), 0, null, null);

        CL.clReleaseMemObject(memText);
        CL.clReleaseMemObject(memOffsets);
        CL.clReleaseMemObject(memTarget);
        CL.clReleaseMemObject(memOut);

        int total = 0; for (int r : results) total += r; return total;
    }
}
