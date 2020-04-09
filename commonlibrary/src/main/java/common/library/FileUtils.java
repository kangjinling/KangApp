package common.library;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static final String DST_FOLDER_NAME = "APP_File";

    /**
     * 初始化保存路径
     *
     * @return
     */
    public static String initPath() {
        if (TextUtils.isEmpty(storagePath)) {
            storagePath = parentPath.getAbsolutePath() + "/" + DST_FOLDER_NAME;
        }
        File f = new File(storagePath);
        if (!f.exists()) {
            f.mkdir();
        }
        return storagePath;
    }

    /**
     * @return
     */
    public static String getSavePath(String fileName) {
        File file = new File(initPath() + "/" + fileName+"/");
        if (file.mkdirs()) {
            return file.toString();
        }
        return file.toString();
    }




    public static void delSingleFile(File file) {
        deleteFile(file);
    }

    private static boolean deleteFile(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return true;
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    deleteFile(f);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] readFile(String file) {
        // 需要读取的文件，参数是文件的路径名加文件名
        if (!file.equals("")) {
            // 以字节流方法读取文件
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                // 设置一个，每次 装载信息的容器
                byte[] buffer = new byte[1024];
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 开始读取数据
                int len = 0;// 每次读取到的数据的长度
                while ((len = fis.read(buffer)) != -1) {// len值为-1时，表示没有数据了
                    // append方法往sb对象里面添加数据
                    outputStream.write(buffer, 0, len);
                }
                // 输出字符串
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("文件不存在！");
        }
        return null;
    }


    /**
     * 查找指定文件夹中的文件
     * @param fileAbsolutePath
     * @return
     */
    public static List<String> queryFileName(String fileAbsolutePath) {
        List<String> vecFile = new ArrayList<String>();
        File file = new File(fileAbsolutePath);
        if (!file.exists()){
            return vecFile;
        }
        File[] subFile = file.listFiles();
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                vecFile.add(filename);
            }
        }
        return vecFile;
    }

    public static String showUpload(String urlpath, String filePath, String name) throws Exception {
        URL u = new URL(urlpath);
        String path = filePath+ name;
        byte[] buffer = new byte[1024 * 8];
        int read;
//        int ava = 0;
//        long start = System.currentTimeMillis();
        BufferedInputStream bin = null;
        HttpURLConnection urlcon = null;
        BufferedOutputStream bout = null;
        try {
            urlcon = (HttpURLConnection) u.openConnection();
            // double fileLength = (double) urlcon.getContentLength();
            bin = new BufferedInputStream(u.openStream());
            bout = new BufferedOutputStream(new FileOutputStream(path));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
//                ava += read;
//                int a = (int) Math.floor((ava / fileLength * 100));
//                //dialog.setProgress(a);
//                long speed = ava / (System.currentTimeMillis() - start);
//                System.out.println("Download: " + ava + " byte(s)"+ " avg speed: " + speed + "  (kb/s)");
            }
            bout.flush();
            return path;
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            bout.close();
            bin.close();
            urlcon.disconnect();
        }
        return "";
    }

    /**
     * 往文件中写入文本内容
     * @param filename
     * @param filecontent
     * @throws Exception
     */
    public static void savaFileToSD(String filename, String filecontent)  {
        try {
            //如果手机已插入sd卡,且app具有读写sd卡的权限
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filename = FileUtils.getSavePath("Logs") + "/" + filename;

                //这里就不要用openFileOutput了,那个是往手机内存中写数据的
                FileOutputStream output = new FileOutputStream(filename);
                output.write(filecontent.getBytes());
                //将String字符串以字节流的形式写入到输出流中
                output.close();
                //关闭输出流

            } else {
                //Toast.makeText(BaseApplication.getContext(), "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
    }


    /**
     * 将文本写入文件并换行
     * @param strcontent
     * @param fileName
     */
    public static void writeTextToFile(String strcontent, String fileName){
        //先生成文件夹再生成文件，不然会报错
        String strFilePath = FileUtils.getSavePath("Logs") +  "/" + fileName;
        //每次写入时都换行
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()){
                Log.e("TestFile","Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile rf = new RandomAccessFile(file,"rwd");
            rf.seek(file.length());
            rf.write(strContent.getBytes());
            Log.e("cacacaca","成功写入,strContent:"+strContent+"filePath:" +"fileName:"+fileName);
            rf.close();
        }catch (Exception e){
            Log.e("TestFile","Error on write File:"+e);
        }
    }
}
