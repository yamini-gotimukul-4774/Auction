package edu.utexas.ece.mwf.auction.userInterface;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import edu.utexas.ece.mwf.auction.client.Buyer;
import edu.utexas.ece.mwf.auction.event.BidEvent;
import edu.utexas.ece.mwf.auction.event.ItemAvailableEvent;
import edu.utexas.ece.mwf.auction.subscription.ItemInterestSubscription;

public class BuyerFrame extends JFrame {

    private JPanel contentPane;
    private JTextField txtItemTag;
    private JTextField txtMinimumBid;
    private JTextField txtItemName;
    private JTable table;
    private JTextField txtbidAmount;
    private String itemName;
    private String itemTag;
    private String itemType;
    private Double minimumBid;
    private Boolean manual;
    private Double bidIncrement;
    private Double maximumBidAmount;
    private ItemInterestSubscription itemInterestSubscription;
    private ItemAvailableEvent itemAvailableEvent;
    private JTextField textField;
    private JTextField textField_1;
    private JLabel lblBidIncrement;
    private JLabel lblMaximumBidAmount;
    private JLabel lblItemId;
    private JLabel lblMaximumBid;
    private JRadioButton rdbtnManualMode;
    private JRadioButton rdbtnAutomaticMode;
    private JComboBox<Long> comboBox_itemId;
    private JComboBox<String> comboBox_itemType;
    private Long itemSelectedForBid;
    private Double bidAmount;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BuyerFrame frame = new BuyerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the Buyer frame.
     */
    public BuyerFrame() {

        final Buyer buyer = new Buyer();

        try {
            buyer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("BUYER USER INTERFACE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 968, 820);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBounds(12, 12, 913, 31);
        contentPane.add(panel);

        JLabel lblBuyerInformation = new JLabel("BUYER INFORMATION");
        lblBuyerInformation.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel.add(lblBuyerInformation);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 466, 922, 267);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null},
                {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null},
                {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null},
                {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null}, {null, null, null, null, null, null},}, new String[]{
                "UNIQUE ID", "ITEM NAME", "ITEM TYPE", "ITEM TAG", "MINIMUM BID", "CURRENT BID"}));
        table.getColumnModel().getColumn(4).setPreferredWidth(108);
        scrollPane.setViewportView(table);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_1.setBackground(Color.LIGHT_GRAY);
        panel_1.setBounds(12, 434, 928, 31);
        contentPane.add(panel_1);

        JLabel lblAvailableItemsMatching = new JLabel("AVAILABLE ITEMS MATCHING THE INTEREST");
        lblAvailableItemsMatching.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_1.add(lblAvailableItemsMatching);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_2.setBackground(Color.GRAY);
        panel_2.setBounds(12, 400, 928, 31);
        contentPane.add(panel_2);

        JLabel lblUserDrivenMode = new JLabel("USER DRIVEN MODE OF BUYING");
        lblUserDrivenMode.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_2.add(lblUserDrivenMode);

        JDesktopPane desktopPane_1 = new JDesktopPane();
        desktopPane_1.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        desktopPane_1.setBackground(Color.LIGHT_GRAY);
        desktopPane_1.setBounds(473, 44, 452, 342);
        contentPane.add(desktopPane_1);

        txtbidAmount = new JTextField();
        txtbidAmount.setColumns(10);
        txtbidAmount.setBounds(210, 125, 179, 36);
        desktopPane_1.add(txtbidAmount);

        JButton btnStartAutomaticBidding = new JButton("PLACE A BID");
        btnStartAutomaticBidding.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemSelectedForBid = (Long) comboBox_itemId.getSelectedItem();
                bidAmount = Double.valueOf(txtbidAmount.getText());
                buyer.publishBidEvent(itemSelectedForBid, bidAmount);
                txtbidAmount.setText("");
            }
        });
        btnStartAutomaticBidding.setBounds(86, 305, 281, 25);
        desktopPane_1.add(btnStartAutomaticBidding);

        lblItemId = new JLabel("ITEM ID :");
        lblItemId.setBounds(12, 76, 121, 15);
        desktopPane_1.add(lblItemId);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_3.setBackground(Color.GRAY);
        panel_3.setBounds(0, 0, 452, 31);
        desktopPane_1.add(panel_3);

        JLabel lblAutomaticModeOf = new JLabel("PLACE A BID");
        lblAutomaticModeOf.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_3.add(lblAutomaticModeOf);

        lblMaximumBid = new JLabel("BID AMOUNT :");
        lblMaximumBid.setBounds(14, 125, 148, 38);
        desktopPane_1.add(lblMaximumBid);

        comboBox_itemId = new JComboBox<Long>();
        comboBox_itemId.setBounds(210, 73, 179, 20);
        desktopPane_1.add(comboBox_itemId);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBounds(12, 44, 449, 342);
        contentPane.add(desktopPane);
        desktopPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        desktopPane.setBackground(Color.LIGHT_GRAY);

        txtItemTag = new JTextField();
        txtItemTag.setColumns(10);
        txtItemTag.setBounds(210, 104, 179, 19);
        desktopPane.add(txtItemTag);

        txtMinimumBid = new JTextField();
        txtMinimumBid.setColumns(10);
        txtMinimumBid.setBounds(211, 152, 178, 19);
        desktopPane.add(txtMinimumBid);

        txtItemName = new JTextField();
        txtItemName.setColumns(10);
        txtItemName.setBounds(210, 43, 179, 19);
        desktopPane.add(txtItemName);

        JLabel lblBuyerName = new JLabel("Item Name :");
        lblBuyerName.setBounds(12, 45, 100, 15);
        desktopPane.add(lblBuyerName);

        JLabel lblItemType = new JLabel("Item Type: (NEW/OLD)");
        lblItemType.setBounds(12, 76, 180, 15);
        desktopPane.add(lblItemType);

        JLabel lblItemTag = new JLabel("Item tag:");
        lblItemTag.setBounds(12, 106, 125, 30);
        desktopPane.add(lblItemTag);

        JLabel label_5 = new JLabel("Minimum bid in $");
        label_5.setBounds(12, 154, 120, 15);
        desktopPane.add(label_5);

        JButton btnInterested = new JButton("CREATE INTEREST");
        btnInterested.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemName = txtItemName.getText();
                itemTag = txtItemTag.getText();
                itemType = (String) comboBox_itemType.getSelectedItem();
                if (txtMinimumBid.getText() != null && !(txtMinimumBid.getText().trim()).isEmpty()) {
                    minimumBid = Double.valueOf(txtMinimumBid.getText());
                } else {
                    minimumBid = 0.0;
                }

                manual = rdbtnManualMode.isSelected();
                if (!manual) {
                    bidIncrement = Double.valueOf(textField.getText());
                    maximumBidAmount = Double.valueOf(textField_1.getText());
                }
                itemInterestSubscription = new ItemInterestSubscription(itemName, itemType, itemTag, minimumBid, manual, bidIncrement, maximumBidAmount);
                buyer.subscribeForAvailableItem(itemName, itemType, itemTag, minimumBid, manual, bidIncrement, maximumBidAmount);
                setTableData(buyer);
                txtItemName.setText("");
                txtItemTag.setText("");
                txtMinimumBid.setText("");
                textField.setText("");
                textField_1.setText("");
            }
        });

        btnInterested.setBounds(261, 305, 176, 25);
        desktopPane.add(btnInterested);

        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_4.setBackground(Color.GRAY);
        panel_4.setBounds(0, 0, 449, 31);
        desktopPane.add(panel_4);

        JLabel lblRequestForNew = new JLabel("REQUEST FOR NEW INTERESTS");
        lblRequestForNew.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_4.add(lblRequestForNew);

        JLabel lblBidType = new JLabel("Bid type");
        lblBidType.setBounds(22, 192, 70, 15);
        desktopPane.add(lblBidType);

        rdbtnManualMode = new JRadioButton("Manual mode");
        rdbtnManualMode.setSelected(true);
        rdbtnManualMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                lblBidIncrement.setEnabled(false);
                textField.setEnabled(false);
                lblMaximumBidAmount.setEnabled(false);
                textField_1.setEnabled(false);
                lblItemId.setEnabled(true);
                comboBox_itemId.setEnabled(true);
                lblMaximumBid.setEnabled(true);
                txtbidAmount.setEnabled(true);
                rdbtnAutomaticMode.setSelected(false);
            }
        });

        rdbtnManualMode.setBounds(123, 192, 149, 23);
        desktopPane.add(rdbtnManualMode);

        rdbtnAutomaticMode = new JRadioButton("Automatic mode");
        rdbtnAutomaticMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblBidIncrement.setEnabled(true);
                textField.setEnabled(true);
                lblMaximumBidAmount.setEnabled(true);
                textField_1.setEnabled(true);
                lblItemId.setEnabled(false);
                comboBox_itemId.setEnabled(false);
                lblMaximumBid.setEnabled(false);
                txtbidAmount.setEnabled(false);
                rdbtnManualMode.setSelected(false);
            }
        });
        rdbtnAutomaticMode.setBounds(292, 192, 149, 23);
        desktopPane.add(rdbtnAutomaticMode);

        lblBidIncrement = new JLabel("Bid increment");
        lblBidIncrement.setEnabled(false);
        lblBidIncrement.setBounds(22, 238, 105, 19);
        desktopPane.add(lblBidIncrement);

        textField = new JTextField();
        textField.setEnabled(false);
        textField.setBounds(210, 238, 179, 19);
        desktopPane.add(textField);
        textField.setColumns(10);

        lblMaximumBidAmount = new JLabel("Maximum Bid Amount");
        lblMaximumBidAmount.setEnabled(false);
        lblMaximumBidAmount.setBounds(22, 271, 158, 15);
        desktopPane.add(lblMaximumBidAmount);

        textField_1 = new JTextField();
        textField_1.setEnabled(false);
        textField_1.setBounds(210, 269, 179, 19);
        desktopPane.add(textField_1);
        textField_1.setColumns(10);

        comboBox_itemType = new JComboBox<String>();
        comboBox_itemType.setBounds(210, 68, 179, 24);
        comboBox_itemType.addItem("NEW");
        comboBox_itemType.addItem("OLD");
        desktopPane.add(comboBox_itemType);
    }

    public void setTableData(Buyer buyer) {
        reset();
        Set<Long> itemIdSet = buyer.getItemAvailableEventMap().keySet();
        java.util.List<Long> itemIdList = new ArrayList<Long>(itemIdSet);
        for (int row = 0; row < itemIdList.size(); row++) {
            itemAvailableEvent = buyer.getItemAvailableEventMap().get(itemIdList.get(row));
            table.getModel().setValueAt(itemAvailableEvent.getItemId(), row, 0);
            table.getModel().setValueAt(itemAvailableEvent.getName(), row, 1);
            table.getModel().setValueAt(itemAvailableEvent.getType(), row, 2);
            table.getModel().setValueAt(itemAvailableEvent.getTag(), row, 3);
            table.getModel().setValueAt(itemAvailableEvent.getMinimumBid(), row, 4);
            BidEvent updateBidEvent = buyer.getUpdateBidEventMap().get(itemAvailableEvent.getItemId());
            BidEvent newBidEvent = buyer.getNewBidEventMap().get(itemAvailableEvent.getItemId());
            Double updateBidPrice = (updateBidEvent == null) ? 0 : updateBidEvent.getPrice();
            Double newBidPrice = (newBidEvent == null) ? 0 : newBidEvent.getPrice();
            BidEvent bidEvent = (updateBidPrice > newBidPrice) ? updateBidEvent : newBidEvent;

            if (bidEvent != null) {
                table.getModel().setValueAt(bidEvent.getPrice(), row, 5);
            }

            // table.getModel().setValueAt(itemAvailableEvent.getMinimumBid(),
            // row, 4);
            comboBox_itemId.addItem(itemAvailableEvent.getItemId());

        }
    }

    private void reset() {
        int rowCount = table.getModel().getRowCount();
        for (int row = 0; row < rowCount; row++) {
            table.getModel().setValueAt("", row, 0);
            table.getModel().setValueAt("", row, 1);
            table.getModel().setValueAt("", row, 2);
            table.getModel().setValueAt("", row, 3);
            table.getModel().setValueAt("", row, 4);
            table.getModel().setValueAt("", row, 5);
        }

        rowCount = comboBox_itemId.getItemCount();
        for (int row = rowCount - 1; row >= 0; row--) {
            comboBox_itemId.removeItemAt(row);
        }
    }
}
