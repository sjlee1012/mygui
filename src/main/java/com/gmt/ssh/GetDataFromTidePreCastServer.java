package com.gmt.ssh;

import com.jcraft.jsch.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GetDataFromTidePreCastServer {

    public static void main(String[] args) {
        String host = "10.211.55.5";
        int port = 22;
        String username = "parallels";
        String password = "lee4173GF!";

        // 로컬 및 원격 파일 경로를 가져오기 위한 메서드
        List<String> localPaths = makeDirectoryAndPath()[0];
        List<String> remotePaths = makeDirectoryAndPath()[1];

        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp sftpChannel = null;

        try {
            // SSH 세션 설정
            session = jsch.getSession(username, host, port);
            session.setPassword(password);

            // 호스트 키 검사 비활성화
            session.setConfig("StrictHostKeyChecking", "no");

            // SSH 연결 시작
            session.connect();
            System.out.println("SSH 연결이 성공적으로 열렸습니다.");

            // SFTP 채널 열기
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;
            System.out.println("SFTP 클라이언트가 활성화되었습니다.");

            // 파일 다운로드
            for (int i = 0; i < remotePaths.size(); i++) {
                String remotePath = remotePaths.get(i);
                String localPath = localPaths.get(i);

                try {
                    System.out.println("다운로드 중: " + remotePath + " -> " + localPath);
                    File localFile = new File(localPath);
                    try (FileOutputStream fos = new FileOutputStream(localFile)) {
                        sftpChannel.get(remotePath, fos);
                    }
                    System.out.println("다운로드 완료: " + remotePath);
                } catch (Exception e) {
                    System.out.println("다운로드 실패: " + remotePath + " -> " + localPath + ": " + e.getMessage());
                }
            }

        } catch (JSchException e) {
            System.out.println("SSH 또는 SFTP 연결 실패: " + e.getMessage());
        } finally {
            // 자원 정리
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
                System.out.println("SFTP 클라이언트가 닫혔습니다.");
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
                System.out.println("SSH 세션이 닫혔습니다.");
            }
        }
    }

    // 로컬 및 원격 경로 생성 예제
    private static List<String>[] makeDirectoryAndPath() {
        // 로컬 및 원격 파일 경로를 생성하거나 반환
        List<String> localPaths = List.of("file1.txt", "file2.txt");
        List<String> remotePaths = List.of("file1.txt", "file2.txt");
        return new List[]{localPaths, remotePaths};
    }
}
