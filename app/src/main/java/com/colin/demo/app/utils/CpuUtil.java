package com.colin.demo.app.utils;


import android.annotation.SuppressLint;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CpuUtil {
    //CPU核数
    public static final String CPU_BASE_PATH = "/sys/devices/system/cpu/";
    public static final String CPU_CORES_NUMBER = CPU_BASE_PATH;
    //CPU 最大频率
    public static final String CPU_MAX_FREQ = CPU_BASE_PATH + "cpu0/cpufreq/cpuinfo_max_freq";
    //CPU 最小频率
    public static final String CPU_MIN_FREQ = CPU_BASE_PATH + "cpu0/cpufreq/cpuinfo_min_freq";
    //CPU 频率档位
    public static final String SCALING_AVAILABLE_FREQUENCIES = CPU_BASE_PATH + "cpu0/cpufreq/scaling_available_frequencies";
    //CPU 调频策略
    public static final String SCALING_GOVERNOR = CPU_BASE_PATH + "cpu0/cpufreq/scaling_governor";
    //CPU 支持的调频策略
    public static final String SCALING_AVAILABLE_GOVERNORS = CPU_BASE_PATH + "cpu0/cpufreq/scaling_available_governors";

    /**
     * CPU信息
     *
     * @return
     */
    public static List<String> getCpuInfo() {
        List<String> result = new ArrayList<>();

        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * It's also good way to get cpu core number
     */
    public static int getCPUCoreNum() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * <p>
     * Source: http://stackoverflow.com/questions/7962155/
     *
     * @return The number of cores, or -1 if failed to get result
     */
    public static int getNumCpuCores() {
        try {
            // Get directory containing CPU info
            File dir = new File(CPU_CORES_NUMBER);
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    // Check if filename is "cpu", followed by a single digit number
                    if (Pattern.matches("cpu[0-9]+", file.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return -1 core
            LogUtil.e("Faeiled to count number of cores, defaulting to -1", e);
            return -1;
        }
    }


    /**
     * CPU 最大频率
     *
     * @return
     */
    public static long getCpuMaxFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(CPU_MAX_FREQ));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 最小频率
     *
     * @return
     */
    public static long getCpuMinFreq() {
        long result = 0L;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(CPU_MIN_FREQ));
            if ((line = br.readLine()) != null) {
                result = Long.parseLong(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static String getCpuAvailableFrequenciesSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_FREQUENCIES));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static List<Long> getCpuAvailableFrequencies() {
        List<Long> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_FREQUENCIES));
            if ((line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    long freq = Long.parseLong(value);
                    result.add(freq);
                }
            }


            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 可调节 CPU 频率档位
     *
     * @return
     */
    public static List<Integer> getCpuFreqList() {
        List<Integer> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_FREQUENCIES));
            if ((line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    result.add(Integer.parseInt(value));
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String[] getCpuAFreqList() {
        String[] result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_FREQUENCIES));
            if ((line = br.readLine()) != null) {
                result = line.split("\\s+");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 调频策略
     *
     * @return
     */
    public static String getCpuGovernor() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_GOVERNOR));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static String getCpuAvailableGovernorsSimple() {
        String result = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_GOVERNORS));
            if ((line = br.readLine()) != null) {
                result = line;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String[] getCpuAvailableGovernorsList() {
        String result[] = null;
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_GOVERNORS));
            if ((line = br.readLine()) != null) {
                result = line.split("\\s+");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CPU 支持的调频策略
     *
     * @return
     */
    public static List<String> getCpuAvailableGovernors() {
        List<String> result = new ArrayList<>();
        try {
            String line;
            BufferedReader br = new BufferedReader(new FileReader(SCALING_AVAILABLE_GOVERNORS));
            if ((line = br.readLine()) != null) {
                String[] list = line.split("\\s+");
                for (String value : list) {
                    result.add(value);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位 HZ
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static List<String> getCpuCurFreq(Context mContext) {
        List<String> result = new ArrayList<>();
        int mCpuCoreNumber = getNumCpuCores();
        BufferedReader br = null;

        try {
            for (int i = 0; i < mCpuCoreNumber; i++) {
                final String path = "/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq";
                File mFile = new File(path);
                if (mFile.exists()) {
                    br = new BufferedReader(new FileReader(path));
                    String line = br.readLine();
                    if (line != null) {
                        result.add(String.format("CPU %1$d : 频率 %2$s Hz", i, line));
                    }
                } else {
                    result.add(String.format("CPU %1$d: 已经停止", i));
                }
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return result;
    }

    /**
     * Get cpu's current frequency
     * unit:KHZ
     * 获取cpu当前频率,单位 HZ
     *
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static List<String> getCpuOnlineStatus(Context mContext) {
        List<String> result = new ArrayList<>();
        int mCpuCoreNumber = getNumCpuCores();
        BufferedReader br = null;

        try {
            for (int i = 0; i < mCpuCoreNumber; i++) {
                br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu" + i + "/online"));
                String line = br.readLine();
                if (line != null) {
                    result.add(String.format("CPU %1$d 开关状态: %2$s", i, ("1".equals(line) ? "开启" : "关闭")));
                }

                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * CPU 场景配置文件
     *
     * @return
     */
    public static List<String> getCpuSceneInfo() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/system/vendor/etc/perfservscntbl.txt"));
            result.add("/system/vendor/etc/perfservscntbl.txt");
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            result.add("/system/vendor/etc/perf_whitelist_cfg.xml");
            br = new BufferedReader(new FileReader("/system/vendor/etc/perf_whitelist_cfg.xml"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * CPU 电压
     *
     * @return
     */
    public static List<String> getCpuVoltage() {
        List<String> result = new ArrayList<>();
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/proc/cpufreq/MT_CPU_DVFS_LL/cpufreq_oppidx"));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

            br.close();
        } catch (FileNotFoundException e) {
            result.add(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}