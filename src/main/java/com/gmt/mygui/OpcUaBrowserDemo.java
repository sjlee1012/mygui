package com.gmt.mygui;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class OpcUaBrowserDemo extends JFrame {

    private JTree nodeTree;
    private DefaultTableModel attributeTableModel;
    private DefaultTableModel referenceTableModel;
    private JTable attributeTable;
    private JTable referenceTable;

    public OpcUaBrowserDemo() {
        super("OPC UA Browser Demo");

        // 메인 컨테이너 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // 왼쪽 트리 구성
        DefaultMutableTreeNode rootNode = createSampleOpcUaTree();
        nodeTree = new JTree(rootNode);
        nodeTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode =
                        (DefaultMutableTreeNode) nodeTree.getLastSelectedPathComponent();
                if (selectedNode == null) return;

                // 트리 노드와 연결된 NodeInfo 객체가 있다면 해당 정보로 테이블 갱신
                Object userObject = selectedNode.getUserObject();
                if (userObject instanceof NodeInfo) {
                    NodeInfo nodeInfo = (NodeInfo) userObject;
                    updateTables(nodeInfo);
                } else {
                    // 루트 등 특별한 노드면 테이블 초기화
                    clearTables();
                }
            }
        });

        JScrollPane treeScroll = new JScrollPane(nodeTree);

        // 오른쪽 테이블 패널 구성
        JPanel tablePanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // Attribute 테이블
        attributeTableModel = new DefaultTableModel(
                new Object[]{"Attribute", "Value"}, 0
        );
        attributeTable = new JTable(attributeTableModel);
        tablePanel.add(new JScrollPane(attributeTable));

        // Reference 테이블
        referenceTableModel = new DefaultTableModel(
                new Object[]{"ReferenceType", "Target"}, 0
        );
        referenceTable = new JTable(referenceTableModel);
        tablePanel.add(new JScrollPane(referenceTable));

        // 스플릿패인으로 트리와 테이블 구분
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                treeScroll,
                tablePanel
        );
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * 트리에 표시할 샘플 OPC UA 노드 구조 생성
     */
    private DefaultMutableTreeNode createSampleOpcUaTree() {
        // 실제로는 OPC UA 서버에서 받아온 노드들로 트리를 구성하게 됨.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Objects");

        // 하위 예시 노드들
        DefaultMutableTreeNode myObjectsNode = new DefaultMutableTreeNode("MyObjects");
        root.add(myObjectsNode);

        DefaultMutableTreeNode myDeviceNode = new DefaultMutableTreeNode("MyDevice");
        myObjectsNode.add(myDeviceNode);

        // MyLevel 노드
        NodeInfo myLevelInfo = new NodeInfo(
                "MyLevel",
                Arrays.asList(
                        new AttributePair("NodeId", "ns=6;s=MyLevel"),
                        new AttributePair("NodeClass", "Variable"),
                        new AttributePair("BrowseName", "6:MyLevel"),
                        new AttributePair("Value", "46.0"),
                        new AttributePair("DataType", "Double"),
                        new AttributePair("Historizing", "true"),
                        new AttributePair("AccessLevel", "[CurrentRead, CurrentWrite]")
                ),
                Arrays.asList(
                        new ReferencePair("HasTypeDefinition", "BaseDataVariableType"),
                        new ReferencePair("HasCondition", "MyLevelAlarm")
                )
        );
        DefaultMutableTreeNode myLevelNode = new DefaultMutableTreeNode(myLevelInfo);
        myDeviceNode.add(myLevelNode);

        // MyLevelAlarm 노드 (간단 예시)
        NodeInfo myLevelAlarmInfo = new NodeInfo(
                "MyLevelAlarm",
                Arrays.asList(
                        new AttributePair("NodeId", "ns=6;s=MyLevelAlarm"),
                        new AttributePair("NodeClass", "Object"),
                        new AttributePair("BrowseName", "6:MyLevelAlarm")
                ),
                Arrays.asList(
                        new ReferencePair("HasNotifier", "Server"),
                        new ReferencePair("HasEventSource", "MyLevel")
                )
        );
        DefaultMutableTreeNode myLevelAlarmNode = new DefaultMutableTreeNode(myLevelAlarmInfo);
        myDeviceNode.add(myLevelAlarmNode);

        // MyMethod 노드
        NodeInfo myMethodInfo = new NodeInfo(
                "MyMethod",
                Arrays.asList(
                        new AttributePair("NodeId", "ns=6;s=MyMethod"),
                        new AttributePair("NodeClass", "Method"),
                        new AttributePair("BrowseName", "6:MyMethod")
                ),
                Arrays.asList(
                        new ReferencePair("HasProperty", "Executable"),
                        new ReferencePair("HasProperty", "UserExecutable")
                )
        );
        DefaultMutableTreeNode myMethodNode = new DefaultMutableTreeNode(myMethodInfo);
        myDeviceNode.add(myMethodNode);

        // MySwitch 노드
        NodeInfo mySwitchInfo = new NodeInfo(
                "MySwitch",
                Arrays.asList(
                        new AttributePair("NodeId", "ns=6;s=MySwitch"),
                        new AttributePair("NodeClass", "Variable"),
                        new AttributePair("BrowseName", "6:MySwitch"),
                        new AttributePair("Value", "true"),
                        new AttributePair("DataType", "Boolean")
                ),
                Arrays.asList(
                        new ReferencePair("HasTypeDefinition", "BaseDataVariableType")
                )
        );
        DefaultMutableTreeNode mySwitchNode = new DefaultMutableTreeNode(mySwitchInfo);
        myDeviceNode.add(mySwitchNode);

        // 기타 노드들은 필요에 따라 추가...
        return root;
    }

    /**
     * 테이블을 NodeInfo 객체 정보로 갱신
     */
    private void updateTables(NodeInfo nodeInfo) {
        // Attribute 테이블 갱신
        attributeTableModel.setRowCount(0);
        for (AttributePair attr : nodeInfo.attributes) {
            attributeTableModel.addRow(new Object[]{attr.key, attr.value});
        }

        // Reference 테이블 갱신
        referenceTableModel.setRowCount(0);
        for (ReferencePair ref : nodeInfo.references) {
            referenceTableModel.addRow(new Object[]{ref.referenceType, ref.target});
        }
    }

    private void clearTables() {
        attributeTableModel.setRowCount(0);
        referenceTableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OpcUaBrowserDemo demo = new OpcUaBrowserDemo();
            demo.setVisible(true);
        });
    }

    // 노드 하나의 속성과 레퍼런스 정보를 담는 단순 POJO
    static class NodeInfo {
        String displayName;
        List<AttributePair> attributes;
        List<ReferencePair> references;

        NodeInfo(String displayName, List<AttributePair> attributes, List<ReferencePair> references) {
            this.displayName = displayName;
            this.attributes = attributes;
            this.references = references;
        }

        @Override
        public String toString() {
            // 트리에 표시할 이름
            return displayName;
        }
    }

    static class AttributePair {
        String key;
        String value;

        AttributePair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    static class ReferencePair {
        String referenceType;
        String target;

        ReferencePair(String referenceType, String target) {
            this.referenceType = referenceType;
            this.target = target;
        }
    }
}
