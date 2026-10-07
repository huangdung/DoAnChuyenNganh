import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class LopHocFrame extends JDialog {

    private final QuanLyHocSinh manager;
    private final String currentRole;
    private final Runnable onDataChanged;

    private JTextField txtMaLop;
    private JTextField txtTenLop;
    private JTextField txtGvcn;
    private JTextField txtPhongHoc;
    private JLabel lblSiSoVal;

    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;

    private JTextField txtTimKiem;
    private JTable tblLop;
    private DefaultTableModel modelLop;

    private String selectedOldMaLop = null;

    public LopHocFrame(Frame parent, boolean modal, QuanLyHocSinh manager, String currentRole, Runnable onDataChanged) {
        super(parent, "QUẢN LÝ DANH MỤC LỚP HỌC & GVCN", modal);
        this.manager = manager;
        this.currentRole = currentRole;
        this.onDataChanged = onDataChanged;

        initUI();
        loadData();
    }

    private void initUI() {
        setSize(890, 590);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(0, 10));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        headerPanel.setBackground(new Color(27, 79, 114));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel("QUẢN LÝ DANH MỤC LỚP HỌC & GIÁO VIÊN CHỦ NHIỆM", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Thêm mới, cập nhật, xóa và tra cứu danh mục lớp học & GVCN toàn trường", JLabel.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(214, 234, 248));

        headerPanel.add(lblTitle);
        headerPanel.add(lblSub);
        add(headerPanel, BorderLayout.NORTH);

        JPanel bodyPanel = new JPanel(new BorderLayout(14, 0));
        bodyPanel.setBackground(new Color(245, 247, 250));
        bodyPanel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JPanel pnlForm = new JPanel(new BorderLayout(0, 12));
        pnlForm.setPreferredSize(new Dimension(340, 0));
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblFormTitle = new JLabel("THÔNG TIN LỚP HỌC", JLabel.LEFT);
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFormTitle.setForeground(new Color(41, 128, 185));
        pnlForm.add(lblFormTitle, BorderLayout.NORTH);

        JPanel pnlFields = new JPanel(new GridLayout(5, 2, 8, 12));
        pnlFields.setOpaque(false);

        JLabel lblM = new JLabel("Mã Lớp (*):");
        lblM.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtMaLop = new JTextField();
        txtMaLop.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtMaLop.setToolTipText("Mã lớp viết liền không dấu (VD: 10A1, 12A2...)");

        JLabel lblT = new JLabel("Tên Lớp (*):");
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTenLop = new JTextField();
        txtTenLop.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTenLop.setToolTipText("Tên gọi chi tiết (VD: Lớp 10A1 (Chuyên Toán))");

        JLabel lblG = new JLabel("GVCN (*):");
        lblG.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtGvcn = new JTextField();
        txtGvcn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtGvcn.setToolTipText("Họ và tên Giáo viên chủ nhiệm");

        JLabel lblP = new JLabel("Phòng Học:");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtPhongHoc = new JTextField();
        txtPhongHoc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtPhongHoc.setToolTipText("Vị trí phòng học (VD: Phòng 301, Nhà A)");

        JLabel lblSS = new JLabel("Sĩ Số Hiện Tại:");
        lblSS.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSiSoVal = new JLabel("0 học sinh");
        lblSiSoVal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSiSoVal.setForeground(new Color(39, 174, 96));

        pnlFields.add(lblM); pnlFields.add(txtMaLop);
        pnlFields.add(lblT); pnlFields.add(txtTenLop);
        pnlFields.add(lblG); pnlFields.add(txtGvcn);
        pnlFields.add(lblP); pnlFields.add(txtPhongHoc);
        pnlFields.add(lblSS); pnlFields.add(lblSiSoVal);
        pnlForm.add(pnlFields, BorderLayout.CENTER);

        JPanel pnlBtns = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlBtns.setOpaque(false);

        btnThem = new JButton("Thêm Mới");
        styleButton(btnThem, new Color(39, 174, 96), Color.WHITE);

        btnSua = new JButton("Cập Nhật");
        styleButton(btnSua, new Color(230, 126, 34), Color.WHITE);

        btnXoa = new JButton("Xóa Lớp");
        styleButton(btnXoa, new Color(192, 57, 43), Color.WHITE);

        btnLamMoi = new JButton("Làm Mới");
        styleButton(btnLamMoi, new Color(52, 73, 94), Color.WHITE);

        boolean isAdmin = "Admin".equalsIgnoreCase(currentRole);
        if (!isAdmin) {
            setButtonDisabled(btnThem, "🔒 Chỉ Admin mới có quyền Thêm lớp");
            setButtonDisabled(btnSua, "🔒 Chỉ Admin mới có quyền Sửa lớp");
            setButtonDisabled(btnXoa, "🔒 Chỉ Admin mới có quyền Xóa lớp");
        }

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);
        pnlBtns.add(btnLamMoi);
        pnlForm.add(pnlBtns, BorderLayout.SOUTH);
        bodyPanel.add(pnlForm, BorderLayout.WEST);

        JPanel pnlRight = new JPanel(new BorderLayout(0, 8));
        pnlRight.setOpaque(false);

        JPanel pnlSearch = new JPanel(new BorderLayout(8, 0));
        pnlSearch.setOpaque(false);
        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtTimKiem.setToolTipText("🔍 Tìm theo mã lớp, tên lớp hoặc tên GVCN...");
        JButton btnTim = new JButton("Tìm Kiếm");
        styleButton(btnTim, new Color(41, 128, 185), Color.WHITE);
        btnTim.setPreferredSize(new Dimension(100, 30));
        JButton btnTatCa = new JButton("Tất Cả");
        styleButton(btnTatCa, new Color(127, 140, 141), Color.WHITE);
        btnTatCa.setPreferredSize(new Dimension(80, 30));

        JPanel pnlSearchBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pnlSearchBtns.setOpaque(false);
        pnlSearchBtns.add(btnTim);
        pnlSearchBtns.add(btnTatCa);

        pnlSearch.add(txtTimKiem, BorderLayout.CENTER);
        pnlSearch.add(pnlSearchBtns, BorderLayout.EAST);
        pnlRight.add(pnlSearch, BorderLayout.NORTH);

        String[] cols = {"Mã Lớp", "Tên Lớp", "Giáo Viên Chủ Nhiệm", "Phòng Học", "Sĩ Số"};
        modelLop = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLop = new JTable(modelLop);
        tblLop.setRowHeight(28);
        tblLop.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblLop.setShowGrid(true);
        tblLop.setGridColor(new Color(230, 235, 240));
        tblLop.setSelectionBackground(new Color(52, 152, 219));
        tblLop.setSelectionForeground(Color.WHITE);
        tblLop.getTableHeader().setReorderingAllowed(false);
        tblLop.setDefaultEditor(Object.class, null);

        tblLop.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable tbl, Object val,
                    boolean isSel, boolean hasFoc, int row, int col) {
                java.awt.Component comp = super.getTableCellRendererComponent(tbl, val, isSel, hasFoc, row, col);
                comp.setBackground(new Color(41, 128, 185));
                comp.setForeground(Color.WHITE);
                comp.setFont(new Font("Segoe UI", Font.BOLD, 12));
                ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
                ((javax.swing.JComponent) comp).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(210, 215, 220)));
                return comp;
            }
        });

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblLop.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblLop.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblLop.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        pnlRight.add(new JScrollPane(tblLop), BorderLayout.CENTER);
        bodyPanel.add(pnlRight, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(238, 242, 246));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        JLabel lblNote = new JLabel("ℹ Ràng buộc toàn vẹn: Không thể xóa lớp học nếu đang có học sinh theo học.");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNote.setForeground(new Color(120, 144, 156));

        JButton btnClose = new JButton("Đóng & Hoàn Tất");
        styleButton(btnClose, new Color(41, 128, 185), Color.WHITE);
        btnClose.setPreferredSize(new Dimension(145, 34));
        btnClose.addActionListener(e -> {
            notifyChanged();
            dispose();
        });

        footer.add(lblNote, BorderLayout.WEST);
        footer.add(btnClose, BorderLayout.EAST);
        add(footer, BorderLayout.SOUTH);

        addEvents(btnTim, btnTatCa);
    }

    private void addEvents(JButton btnTim, JButton btnTatCa) {
        
        tblLop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = tblLop.getSelectedRow();
                if (r != -1) {
                    String m = String.valueOf(tblLop.getValueAt(r, 0));
                    String t = String.valueOf(tblLop.getValueAt(r, 1));
                    String g = String.valueOf(tblLop.getValueAt(r, 2));
                    String p = String.valueOf(tblLop.getValueAt(r, 3));
                    txtMaLop.setText(m);
                    txtTenLop.setText(t);
                    txtGvcn.setText(g);
                    txtPhongHoc.setText(p);
                    selectedOldMaLop = m;
                    int siSo = manager.demHocSinhTrongLop(m);
                    lblSiSoVal.setText(siSo + " học sinh");
                }
            }
        });

        
        btnThem.addActionListener(e -> {
            if (!"Admin".equalsIgnoreCase(currentRole)) {
                JOptionPane.showMessageDialog(this, "Tài khoản Giáo viên chỉ có quyền xem danh mục!", "Từ Chối", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String m = txtMaLop.getText().trim();
            String t = txtTenLop.getText().trim();
            String g = txtGvcn.getText().trim();
            String p = txtPhongHoc.getText().trim();

            if (m.isEmpty() || t.isEmpty() || g.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ các trường bắt buộc (*): Mã lớp, Tên lớp, GVCN!", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!m.matches("^[a-zA-Z0-9]+$")) {
                JOptionPane.showMessageDialog(this, "Mã lớp chỉ được chứa chữ cái và số, không chứa khoảng trắng!", "Sai Mã Lớp", JOptionPane.WARNING_MESSAGE);
                txtMaLop.requestFocus();
                return;
            }
            if (manager.tonTaiMaLop(m)) {
                JOptionPane.showMessageDialog(this, "Mã lớp '" + m + "' đã tồn tại! Vui lòng chọn mã khác.", "Trùng Mã Lớp", JOptionPane.ERROR_MESSAGE);
                txtMaLop.requestFocus();
                return;
            }

            manager.themLopHoc(new LopHoc(m, t, g, p));
            loadData();
            notifyChanged();
            JOptionPane.showMessageDialog(this, "Thêm lớp học [" + m + " - " + t + "] thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        
        btnSua.addActionListener(e -> {
            if (!"Admin".equalsIgnoreCase(currentRole)) {
                JOptionPane.showMessageDialog(this, "Tài khoản Giáo viên chỉ có quyền xem danh mục!", "Từ Chối", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int r = tblLop.getSelectedRow();
            if (r == -1 || selectedOldMaLop == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một lớp học trong bảng để cập nhật!", "Chưa Chọn", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String m = txtMaLop.getText().trim();
            String t = txtTenLop.getText().trim();
            String g = txtGvcn.getText().trim();
            String p = txtPhongHoc.getText().trim();

            if (m.isEmpty() || t.isEmpty() || g.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ: Mã lớp, Tên lớp, GVCN!", "Cảnh Báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!m.matches("^[a-zA-Z0-9]+$")) {
                JOptionPane.showMessageDialog(this, "Mã lớp chỉ được chứa chữ cái và số, không chứa khoảng trắng!", "Sai Mã Lớp", JOptionPane.WARNING_MESSAGE);
                txtMaLop.requestFocus();
                return;
            }
            if (!m.equalsIgnoreCase(selectedOldMaLop) && manager.tonTaiMaLop(m)) {
                JOptionPane.showMessageDialog(this, "Mã lớp mới '" + m + "' đã tồn tại ở lớp khác!", "Trùng Mã", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.suaLopHoc(selectedOldMaLop, new LopHoc(m, t, g, p));
            selectedOldMaLop = m;
            loadData();
            notifyChanged();
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin lớp học [" + m + "] thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        });

        
        btnXoa.addActionListener(e -> {
            if (!"Admin".equalsIgnoreCase(currentRole)) {
                JOptionPane.showMessageDialog(this, "Tài khoản Giáo viên chỉ có quyền xem danh mục!", "Từ Chối", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int r = tblLop.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lớp học trong bảng cần xóa!", "Chưa Chọn", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String m = String.valueOf(tblLop.getValueAt(r, 0));
            int siSo = manager.demHocSinhTrongLop(m);
            if (siSo > 0) {
                JOptionPane.showMessageDialog(this, "KHÔNG THỂ XÓA lớp '" + m + "' vì đang có " + siSo + " học sinh đang theo học!\nVui lòng chuyển học sinh sang lớp khác trước khi xóa lớp này.", 
                                              "Ràng Buộc Dữ Liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cf = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lớp '" + m + "' khỏi hệ thống?", "Xác Nhận Xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (cf == JOptionPane.YES_OPTION) {
                manager.xoaLopHoc(m);
                loadData();
                notifyChanged();
                clearForm();
                JOptionPane.showMessageDialog(this, "Đã xóa lớp học thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            }
        });

       
        btnLamMoi.addActionListener(e -> clearForm());

        
        btnTim.addActionListener(e -> loadData());
        txtTimKiem.addActionListener(e -> loadData());
        btnTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            loadData();
        });
    }

    private void clearForm() {
        txtMaLop.setText("");
        txtTenLop.setText("");
        txtGvcn.setText("");
        txtPhongHoc.setText("");
        lblSiSoVal.setText("0 học sinh");
        tblLop.clearSelection();
        selectedOldMaLop = null;
        txtMaLop.requestFocus();
    }

    private void loadData() {
        modelLop.setRowCount(0);
        String kw = txtTimKiem != null ? txtTimKiem.getText().trim().toLowerCase() : "";
        for (LopHoc lh : manager.getDanhSachLopHoc()) {
            if (kw.isEmpty() || lh.getMaLop().toLowerCase().contains(kw)
                    || lh.getTenLop().toLowerCase().contains(kw)
                    || lh.getGvcn().toLowerCase().contains(kw)) {
                int siSo = manager.demHocSinhTrongLop(lh.getMaLop());
                modelLop.addRow(new Object[]{
                    lh.getMaLop(), lh.getTenLop(), lh.getGvcn(), lh.getPhongHoc(), siSo + " HS"
                });
            }
        }
    }

    private void notifyChanged() {
        if (onDataChanged != null) {
            onDataChanged.run();
        }
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setUI(new BasicButtonUI());
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
    }

    private void setButtonDisabled(JButton btn, String tooltip) {
        btn.setEnabled(false);
        btn.setToolTipText(tooltip);
        btn.setBackground(new Color(226, 232, 240));
        btn.setForeground(new Color(148, 163, 184));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}
