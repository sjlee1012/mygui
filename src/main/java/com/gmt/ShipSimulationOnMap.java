package com.gmt;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.*;
import org.geotools.swing.JMapPane;
import org.geotools.api.geometry.BoundingBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ShipSimulationOnMap extends JFrame {
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final double SPEED = 0.005; // 선박 속도 (위도/경도 단위)
    private JMapPane mapPane;
    private Layer shipLayer;
    private double shipLat = 36.5; // 선박 초기 위도
    private double shipLon = 127.5; // 선박 초기 경도
    private double direction = 90; // 방향 (0: 오른쪽, 90: 위쪽, 180: 왼쪽, 270: 아래쪽)

    public ShipSimulationOnMap(File shapeFile) throws Exception {
        // 지도 로드
        FileDataStore store = FileDataStoreFinder.getDataStore(shapeFile);
        MapContent mapContent = new MapContent();
        mapContent.setTitle("Ship Navigation on Map");
        Layer shpLayer = new FeatureLayer(store.getFeatureSource(), null);
        mapContent.addLayer(shpLayer);

        // 선박 레이어 추가
        shipLayer = new MemoryLayer("Ship", Color.GREEN);
        mapContent.addLayer(shipLayer);

        // 지도 UI 설정
        mapPane = new JMapPane();
        mapPane.setMapContent(mapContent);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mapPane, BorderLayout.CENTER);

        // 1초마다 선박 이동
        Timer timer = new Timer(1000, this::moveShip);
        timer.start();

        setTitle("Ship Navigation Simulator");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * 선박 이동 로직
     */
    private void moveShip(ActionEvent e) {
        double angleRad = Math.toRadians(direction);
        shipLat += Math.cos(angleRad) * SPEED;
        shipLon += Math.sin(angleRad) * SPEED;

        ((MemoryLayer) shipLayer).updateShip(shipLat, shipLon);
        mapPane.repaint();
    }

    /**
     * 선박 위치를 저장하고 지도에 표시하는 메모리 레이어
     */
    private static class MemoryLayer extends Layer {
        private String name;
        private Color color;
        private double shipLat, shipLon;

        public MemoryLayer(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public void updateShip(double lat, double lon) {
            this.shipLat = lat;
            this.shipLon = lon;
        }

        //@Override
        public void draw(Graphics2D graphics, org.geotools.renderer.GTRenderer renderer, BoundingBox viewport) {
            graphics.setColor(color);
            int x = (int) ((shipLon - 127.5) * 100000) + 500;
            int y = (int) ((36.5 - shipLat) * 100000) + 400;
            graphics.fillOval(x - 5, y - 5, 10, 10);
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        File shapeFile = new File("./korea.shp"); // SHP 파일 경로
        new ShipSimulationOnMap(shapeFile).setVisible(true);
    }
}

