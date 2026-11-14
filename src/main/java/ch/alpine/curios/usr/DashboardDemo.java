package ch.alpine.curios.usr;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;

import net.miginfocom.swing.MigLayout;

public class DashboardDemo {
  public static void main(String[] args) {
    // 1. Setup FlatLaf Dark Theme and global options
    FlatDarkLaf.setup();
    UIManager.put("Button.arc", 10);
    UIManager.put("Component.arc", 10);
    UIManager.put("TextComponent.arc", 10);
    SwingUtilities.invokeLater(() -> {
      JFrame frame = new JFrame("Modern Analytics Dashboard");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(1100, 700);
      frame.setLocationRelativeTo(null);
      // Root layout: Sidebar on the left, Main content area on the right
      frame.setLayout(new MigLayout("insets 0, gap 0", "[220!][grow]", "[grow]"));
      // --- SIDEBAR PANEL ---
      JPanel sidebar = new JPanel(new MigLayout("wrap, insets 20 15 20 15, gapy 10", "[grow]", "[][][][grow][]"));
//      sidebar.setBackground(new UIManager.ColorTransformer() {
//        @Override
//        public Color deriveColor(Color c) {
//          return c.darker();
//        }
//      }.deriveColor(UIManager.getColor("Panel.background"))); // Slightly darker sidebar
      JLabel logoLabel = new JLabel("⚡ NexusAdmin");
      logoLabel.setFont(logoLabel.getFont().deriveFont(Font.BOLD, 16f));
      sidebar.add(logoLabel, "gapy 0 20");
      sidebar.add(createSidebarButton("📊 Overview", true), "growx");
      sidebar.add(createSidebarButton("👥 Users", false), "growx");
      sidebar.add(createSidebarButton("⚙️ Settings", false), "growx");
      // Push footer to bottom
      JButton logoutBtn = createSidebarButton("🚪 Logout", false);
      logoutBtn.setForeground(new Color(220, 80, 80));
      sidebar.add(logoutBtn, "gapy push, growx");
      // --- MAIN CONTENT AREA ---
      JPanel mainContent = new JPanel(new MigLayout("insets 20, wrap 1, gapy 20", "[grow]", "[][][grow]"));
      // Header Toolbar
      JPanel header = new JPanel(new MigLayout("insets 0", "[grow][pref]", "[]"));
      JLabel titleLabel = new JLabel("Dashboard Overview");
      titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));
      JTextField searchField = new JTextField(20);
      searchField.putClientProperty("JTextField.placeholderText", "Search records...");
      searchField.putClientProperty("JTextField.showClearButton", true);
      header.add(titleLabel, "aligny center");
      header.add(searchField, "alignx right");
      // Metric Cards Grid (3 columns)
      JPanel metricsPanel = new JPanel(new MigLayout("insets 0, gap 15", "[grow][grow][grow]", "[grow]"));
      metricsPanel.add(createCard("Total Revenue", "$48,250", "+12.4% this month"), "grow");
      metricsPanel.add(createCard("Active Users", "1,429", "+3.2% today"), "grow");
      metricsPanel.add(createCard("Server Uptime", "99.98%", "Normal operation"), "grow");
      // Data Table Panel
      JPanel tablePanel = new JPanel(new MigLayout("insets 15, wrap 1, gapy 10", "[grow]", "[][grow]"));
      tablePanel.putClientProperty("JComponent.roundRect", true);
      tablePanel.setBackground(UIManager.getColor("Panel.background"));
      JLabel tableTitle = new JLabel("Recent Transactions");
      tableTitle.setFont(tableTitle.getFont().deriveFont(Font.BOLD, 14f));
      String[] columns = { "ID", "Customer", "Amount", "Status", "Date" };
      Object[][] data = { { "#1092", "Acme Corp", "$1,200.00", "Completed", "2026-06-01" },
          { "#1093", "Globex Corporation", "$850.50", "Pending", "2026-06-02" }, { "#1094", "Soylent Corp", "$3,400.00", "Completed", "2026-06-02" },
          { "#1095", "Initech LLC", "$450.00", "Failed", "2026-06-03" }, { "#1096", "Umbrella Corp", "$1,920.00", "Completed", "2026-06-03" } };
      JTable table = new JTable(new DefaultTableModel(data, columns));
      table.setRowHeight(30);
      table.getTableHeader().setReorderingAllowed(false);
      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      tablePanel.add(tableTitle);
      tablePanel.add(scrollPane, "grow");
      // Assemble main content
      mainContent.add(header, "growx");
      mainContent.add(metricsPanel, "growx");
      mainContent.add(tablePanel, "grow");
      // Add to frame
      frame.add(sidebar, "growy");
      frame.add(mainContent, "grow");
      frame.setVisible(true);
    });
  }

  private static JButton createSidebarButton(String text, boolean active) {
    JButton btn = new JButton(text);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.putClientProperty("JButton.buttonType", "roundRect");
    if (active) {
      btn.setBackground(UIManager.getColor("Component.accentColor"));
      btn.setForeground(Color.WHITE);
    } else {
      // Make button transparent/flat
      btn.setContentAreaFilled(false);
    }
    return btn;
  }

  private static JPanel createCard(String title, String value, String subtext) {
    JPanel card = new JPanel(new MigLayout("insets 15, wrap 1, gapy 5", "[grow]", "[]0[]0[]"));
    // Use FlatLaf card styling (slightly lighter background than main panel)
    card.setBackground(UIManager.getColor("TableHeader.background"));
    card.putClientProperty("JComponent.roundRect", true);
    JLabel titleLbl = new JLabel(title);
    titleLbl.setForeground(UIManager.getColor("Label.disabledForeground"));
    JLabel valLbl = new JLabel(value);
    valLbl.setFont(valLbl.getFont().deriveFont(Font.BOLD, 22f));
    JLabel subLbl = new JLabel(subtext);
    subLbl.setFont(subLbl.getFont().deriveFont(11f));
    subLbl.setForeground(new Color(100, 200, 100)); // Green accent for positive metrics
    card.add(titleLbl);
    card.add(valLbl);
    card.add(subLbl);
    return card;
  }
}
