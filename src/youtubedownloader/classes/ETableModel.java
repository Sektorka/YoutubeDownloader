package youtubedownloader.classes;

import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ETableModel<RowDataType> extends DefaultTableModel{
    protected Vector<RowDataType> rowsData = new Vector<RowDataType>();
    
    public ETableModel() {
    }
    
    public Vector<RowDataType> getRowsData() {
        return rowsData;
    }

    public Vector getColumnIdentifiers() {
        return columnIdentifiers;
    }

    public EventListenerList getListenerList() {
        return listenerList;
    }
    
    public ETableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public ETableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public ETableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public ETableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    public ETableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public void addRow(RowDataType id, Object[] rowData) {
        addRow(id, convertToVector(rowData));
    }

    public void addRow(RowDataType id, Vector rowData) {
        int row = getRowCount();
        insertRow(id, row, rowData);
    }

    public void insertRow(RowDataType id, int row, Object[] rowData) {
        insertRow(id, row, convertToVector(rowData));
    }

    public void insertRow(RowDataType id, int row, Vector rowData) {
        super.insertRow(row, rowData);
        rowsData.insertElementAt(id, row);
    }

    @Override
    public void removeRow(int row) {
        super.removeRow(row);
        rowsData.removeElementAt(row);
    }
    
    public RowDataType getRowDataAt(int row){
        return rowsData.elementAt(row);
    }
    
}
