package com.gmt.common.io.ssh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MakeDirectoryAndPath {

    public static void createDirectoryIfNotExists(Path path) {
        File directory = path.toFile();
        if (!directory.exists()) {
            try {
                if (directory.mkdirs()) {
                    System.out.println("Complete to create a directory: " + path);
                } else {
                    throw new IOException("Failed to create directory.");
                }
            } catch (IOException e) {
                System.out.println("Making a directory failed: " + e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
    }

    public static List<String>[] makeDirectoryAndPath() {
        int nowYear = LocalDate.now().getYear();
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Create directories
        Path dirDataPath = Paths.get(currentDirectory, "DATA");
        createDirectoryIfNotExists(dirDataPath);

        Path dirYearPath = dirDataPath.resolve(String.valueOf(nowYear));
        createDirectoryIfNotExists(dirYearPath);

        Path dirImgPath = dirYearPath.resolve("IMG");
        createDirectoryIfNotExists(dirImgPath);

        Path dirAreaPath = dirImgPath.resolve("AREA");
        createDirectoryIfNotExists(dirAreaPath);

        Path dirArea1Path = dirAreaPath.resolve("AREA1");
        createDirectoryIfNotExists(dirArea1Path);

        Path dirArea2Path = dirAreaPath.resolve("AREA2");
        createDirectoryIfNotExists(dirArea2Path);

        Path dirArea3Path = dirAreaPath.resolve("AREA3");
        createDirectoryIfNotExists(dirArea3Path);

        Path dirTimePath = dirImgPath.resolve("TIME");
        createDirectoryIfNotExists(dirTimePath);

        Path dirBrt1Path = dirTimePath.resolve("BRT1");
        createDirectoryIfNotExists(dirBrt1Path);

        Path dirBrt2Path = dirTimePath.resolve("BRT2");
        createDirectoryIfNotExists(dirBrt2Path);

        Path dirBrt3Path = dirTimePath.resolve("BRT3");
        createDirectoryIfNotExists(dirBrt3Path);

        Path dirBrt4Path = dirTimePath.resolve("BRT4");
        createDirectoryIfNotExists(dirBrt4Path);

        // Source area file paths
        List<String> sourceFileAreaPaths = new ArrayList<>();
        sourceFileAreaPaths.add(dirArea1Path.resolve("AREA1_Surface_Current_Speed_" + currentDate + "00.gif").toString());
        sourceFileAreaPaths.add(dirArea2Path.resolve("AREA2_Surface_Current_Speed_" + currentDate + "00.gif").toString());
        sourceFileAreaPaths.add(dirArea3Path.resolve("AREA3_Surface_Current_Speed_" + currentDate + "00.gif").toString());
        sourceFileAreaPaths.add(dirBrt1Path.resolve("BRT1-CurrentVel_" + currentDate + "00.png").toString());
        sourceFileAreaPaths.add(dirBrt2Path.resolve("BRT2-CurrentVel_" + currentDate + "00.png").toString());
        sourceFileAreaPaths.add(dirBrt3Path.resolve("BRT3-CurrentVel_" + currentDate + "00.png").toString());
        sourceFileAreaPaths.add(dirBrt4Path.resolve("BRT4-CurrentVel_" + currentDate + "00.png").toString());

        // Remote area file paths
        List<String> remoteFileAreaPaths = new ArrayList<>();
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/AREA/AREA1/AREA1_Surface_Current_Speed_" + currentDate + "00.gif");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/AREA/AREA2/AREA2_Surface_Current_Speed_" + currentDate + "00.gif");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/AREA/AREA3/AREA3_Surface_Current_Speed_" + currentDate + "00.gif");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/TIMS/BRT1/BRT1-CurrentVel_" + currentDate + "00.png");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/TIMS/BRT2/BRT2-CurrentVel_" + currentDate + "00.png");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/TIMS/BRT3/BRT3-CurrentVel_" + currentDate + "00.png");
        remoteFileAreaPaths.add("/home/oenergy/FORECAST_T/DOUT/TELE/2024/OPER/UD3D/IMG/TIMS/BRT4/BRT4-CurrentVel_" + currentDate + "00.png");

        return new List[]{sourceFileAreaPaths, remoteFileAreaPaths};
    }
}
