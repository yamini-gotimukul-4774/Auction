package edu.utexas.ece.mwf.auction.userInterface;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import edu.utexas.ece.mwf.auction.client.Seller;
import edu.utexas.ece.mwf.auction.event.BidEvent;
import edu.utexas.ece.mwf.auction.event.ItemAvailableEvent;
import edu.utexas.ece.mwf.auction.event.SaleEvent;

public class SellerFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtItemTag;
    private JTextField txtMinimumBid;
    private JTextField txtSellerName;
    private JTable table_1;
    private JTable table;
    private JTable table_closeSale;
    private String itemName;
    private String itemTag;
    private String itemType;
    private Double minimumBid;
    private JComboBox<String> comboBox_itemType;
    private Object[][] data1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SellerFrame frame = new SellerFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SellerFrame() {
        final Seller seller = new Seller();

        try {
            seller.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("SELLER USER INTERFACE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 898, 787);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        panel.setBounds(23, 19, 847, 31);
        contentPane.add(panel);

        JLabel label_7 = new JLabel("SELLER INFORMATION");
        label_7.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel.add(label_7);

        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        desktopPane.setBackground(Color.LIGHT_GRAY);
        desktopPane.setBounds(28, 62, 423, 225);
        contentPane.add(desktopPane);

        txtItemTag = new JTextField();
        txtItemTag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                itemTag = txtItemTag.getText();
            }
        });
        txtItemTag.setBounds(210, 74, 179, 19);
        desktopPane.add(txtItemTag);
        txtItemTag.setColumns(10);

        txtMinimumBid = new JTextField();
        txtMinimumBid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                minimumBid = Double.valueOf(txtMinimumBid.getText());
            }
        });
        txtMinimumBid.setBounds(210, 135, 178, 19);
        desktopPane.add(txtMinimumBid);
        txtMinimumBid.setColumns(10);

        txtSellerName = new JTextField();
        txtSellerName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

            }
        });
        txtSellerName.setBounds(210, 43, 179, 19);
        desktopPane.add(txtSellerName);
        txtSellerName.setColumns(10);

        JLabel lblSellerName = new JLabel("Item Name :");
        lblSellerName.setBounds(12, 45, 100, 15);
        desktopPane.add(lblSellerName);

        JLabel lblItemName = new JLabel("Item tag");
        lblItemName.setBounds(12, 76, 90, 15);
        desktopPane.add(lblItemName);

        JLabel lblItem = new JLabel("Item type (NEW/OLD)\n");
        lblItem.setBounds(12, 104, 153, 19);
        desktopPane.add(lblItem);

        JLabel lblMinimumBidIn = new JLabel("Minimum bid in $");
        lblMinimumBidIn.setBounds(12, 137, 120, 15);
        desktopPane.add(lblMinimumBidIn);

        JButton btnSellItem = new JButton("Sell Item");
        btnSellItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemName = txtSellerName.getText();
                if (txtMinimumBid.getText() != null && !(txtMinimumBid.getText().trim()).isEmpty()) {
                    minimumBid = Double.valueOf(txtMinimumBid.getText());
                } else {
                    minimumBid = 0.0;
                }

                itemType = (String) comboBox_itemType.getSelectedItem();
                itemTag = txtItemTag.getText();
                seller.publishItemAvailableEvent(itemName, itemType, itemTag, minimumBid);
                setTableData(seller);
                /* Reset the data */
                txtSellerName.setText("");
                txtMinimumBid.setText("");
                txtItemTag.setText("");
            }
        });
        btnSellItem.setBounds(210, 166, 178, 25);
        desktopPane.add(btnSellItem);

        JPanel panel_3 = new JPanel();
        panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_3.setBackground(Color.GRAY);
        panel_3.setBounds(0, 0, 418, 31);
        desktopPane.add(panel_3);

        JLabel lblNewItemFor = new JLabel("NEW ITEM FOR SALE");
        lblNewItemFor.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_3.add(lblNewItemFor);

        comboBox_itemType = new JComboBox<String>();
        comboBox_itemType.setBounds(209, 101, 180, 24);
        comboBox_itemType.addItem("NEW");
        comboBox_itemType.addItem("OLD");
        desktopPane.add(comboBox_itemType);
        // Initializing column headings
		/*
		 * String[] colHeads = new String[] { "SNO", "UNIQUE ID", "ITEM NAME",
		 * "ITEM TYPE", "ITEM TAG", "MINIMUM BID", "CURRENT BID", "STATUS" };
		 * 
		 * Object[][] data = new Object[][] { { null, null, null, null, null,
		 * null, null, null }, { null, null, null, null, null, null, null, null
		 * }, { null, null, null, null, null, null, null, null }, { null, null,
		 * null, null, null, null, null, null }, { null, null, null, null, null,
		 * null, null, null }, { null, null, null, null, null, null, null, null
		 * }, { null, null, null, null, null, null, null, null }, { null, null,
		 * null, null, null, null, null, null }, { null, null, null, null, null,
		 * null, null, null }, { null, null, null, null, null, null, null, null
		 * }, { null, null, null, null, null, null, null, null }, { null, null,
		 * null, null, null, null, null, null }, { null, null, null, null, null,
		 * null, null, null }, { null, null, null, null, null, null, null, null
		 * }, { null, null, null, null, null, null, null, null }, { null, null,
		 * null, null, null, null, null, null }, { null, null, null, null, null,
		 * null, null, null }, { null, null, null, null, null, null, null, null
		 * }, { null, null, null, null, null, null, null, null }, { null, null,
		 * null, null, null, null, null, null }, { null, null, null, null, null,
		 * null, null, null }, };
		 */
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_1.setBackground(Color.LIGHT_GRAY);
        panel_1.setBounds(23, 314, 847, 31);
        contentPane.add(panel_1);

        JLabel label_1 = new JLabel("ACTIVE SALE LIST");
        label_1.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_1.add(label_1);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(18, 357, 854, 346);
        contentPane.add(scrollPane);
        data1 = new Object[][]{{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},};
        String[] colHeads1 = new String[]{"SNO", "UNIQUE ID", "ITEM NAME", "ITEM TYPE", "ITEM TAG", "MINIMUM BID", "CURRENT BID", "STATUS"};
        table = new JTable();
        table.setGridColor(Color.DARK_GRAY);
        table.setModel(new DefaultTableModel(new Object[][]{{null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null}, {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},}, new String[]{"SNO", "UNIQUE ID", "ITEM NAME", "ITEM TYPE", "ITEM TAG", "MINIMUM BID", "CURRENT BID", "STATUS"}) {
            Class[] columnTypes = new Class[]{Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Object.class, Boolean.class};

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        });
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        scrollPane.setViewportView(table);

        JButton button = new JButton("Close A sale");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowCount = table_closeSale.getModel().getRowCount();
                for (int row = 0; row < rowCount; row++) {
                    if ((table.getModel().getValueAt(row, 7)) != null && ((Boolean) table.getModel().getValueAt(row, 7)) == Boolean.TRUE) {
                        seller.publishSaleEvent((Long) (table.getModel().getValueAt(row, 1)), seller.getBrokerConnection().getSelfPort(), (Double) (table.getModel().getValueAt(row, 6)));
                    }
                }

                Set<Long> itemIdSet = seller.getSaleEventMap().keySet();
                java.util.List<Long> itemIdList = new ArrayList<Long>(itemIdSet);
                for (int row = 0; row < itemIdList.size(); row++) {
                    SaleEvent saleEvent = seller.getSaleEventMap().get(itemIdList.get(row));
                    table_closeSale.getModel().setValueAt((saleEvent.getItemId()), row, 0);
                    table_closeSale.getModel().setValueAt(seller.getBrokerConnection().getSelfPort(), row, 1);
                    table_closeSale.getModel().setValueAt((saleEvent.getPrice()), row, 2);
                }
            }
        });

        button.setBounds(696, 715, 142, 25);
        contentPane.add(button);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(463, 92, 407, 195);
        contentPane.add(scrollPane_1);

        table_closeSale = new JTable();
        table_closeSale.setModel(new DefaultTableModel(new Object[][]{{null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}, {null, null, null},
                {null, null, null}, {null, null, null}, {null, null, null}, {null, null, null}, {null, null, null},}, new String[]{"ITEM ID", "BUYER ID", "CLOSING BID"}));
        table_closeSale.getColumnModel().getColumn(2).setPreferredWidth(86);
        scrollPane_1.setViewportView(table_closeSale);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_2.setBackground(Color.LIGHT_GRAY);
        panel_2.setBounds(461, 60, 409, 31);
        contentPane.add(panel_2);

        JLabel lblClosedSaleList = new JLabel("CLOSED SALE LIST");
        lblClosedSaleList.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
        panel_2.add(lblClosedSaleList);
    }

    public void setTableData(Seller seller) {
        reset();
        Set<Long> itemIdSet = seller.getItemAvailableEventMap().keySet();
        java.util.List<Long> itemIdList = new ArrayList<Long>(itemIdSet);
        for (int row = 0; row < itemIdList.size(); row++) {
            ItemAvailableEvent itemAvailableEvent = seller.getItemAvailableEventMap().get(itemIdList.get(row));
            table.getModel().setValueAt(String.valueOf(row + 1), row, 0);
            table.getModel().setValueAt(itemAvailableEvent.getItemId(), row, 1);
            table.getModel().setValueAt(itemAvailableEvent.getName(), row, 2);
            table.getModel().setValueAt(itemAvailableEvent.getType(), row, 3);
            table.getModel().setValueAt(itemAvailableEvent.getTag(), row, 4);
            table.getModel().setValueAt(String.valueOf(itemAvailableEvent.getMinimumBid()), row, 5);
            BidEvent bidEvent = seller.getUpdateBidEventMap().get(itemAvailableEvent.getItemId());
            if (bidEvent != null) {
                table.getModel().setValueAt(bidEvent.getPrice(), row, 6);
            }
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
            table.getModel().setValueAt("", row, 6);
            table.getModel().setValueAt(Boolean.FALSE, row, 7);
        }
    }

}
