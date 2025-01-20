package com.gmt.mygui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpcUaNodeSettingsUI {
    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("OPC UA Node Settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // 메인 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        // 좌측 패널: 트리 뷰 생성
        JPanel treePanel = new JPanel();
        treePanel.setLayout(new BorderLayout());

        JLabel treeLabel = new JLabel("Nodes Browser");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root Node");
        DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("Child Node 1");
        DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("Child Node 2");
        DefaultMutableTreeNode subChild1 = new DefaultMutableTreeNode("Sub Child Node 1");

        root.add(child1);
        root.add(child2);
        child1.add(subChild1);

        JTree nodeTree = new JTree(root);
        nodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane treeScrollPane = new JScrollPane(nodeTree);

        treePanel.add(treeLabel, BorderLayout.NORTH);
        treePanel.add(treeScrollPane, BorderLayout.CENTER);

        // 우측 패널: 노드 설정
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BorderLayout(10, 10));

        JLabel settingsLabel = new JLabel("Node Settings");
        JTextArea settingsArea = new JTextArea();
        settingsArea.setEditable(false);
        JScrollPane settingsScrollPane = new JScrollPane(settingsArea);

        JButton applyButton = new JButton("Apply Settings");

        settingsPanel.add(settingsLabel, BorderLayout.NORTH);
        settingsPanel.add(settingsScrollPane, BorderLayout.CENTER);
        settingsPanel.add(applyButton, BorderLayout.SOUTH);

        // 이벤트 핸들러: 트리 노드 선택 시 설정 내용 표시
        nodeTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) nodeTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                settingsArea.setText("Selected Node: " + selectedNode.getUserObject().toString());
            }
        });

        // Apply 버튼 클릭 이벤트
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String settingsText = settingsArea.getText();
                JOptionPane.showMessageDialog(frame, "Settings Applied:\n" + settingsText);
            }
        });

        // 메인 패널에 좌측(트리) 및 우측(설정) 패널 추가
        mainPanel.add(treePanel, BorderLayout.WEST);
        mainPanel.add(settingsPanel, BorderLayout.CENTER);

        // 프레임에 메인 패널 추가
        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
