package Clases;

import Conexion.Conectar;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Cls_Productos {
    private PreparedStatement PS;
    private ResultSet RS;
    private final Conectar CN;
    private DefaultTableModel DT;
    private final String SQL_INSERT_PRODUCTOS = "INSERT INTO producto (pro_codigo,pro_descripcion, pro_fecha, pro_stock, pro_categoria, pro_precio, pro_referencia, pro_peso) values (?,?,?,?,?,?,?,?)";
    private final String SQL_SELECT_PRODUCTOS = "SELECT *FROM producto";
    
    public Cls_Productos(){
        PS = null;
        CN = new Conectar();
    }
    
    private DefaultTableModel setTitulosProductos(){
        DT = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        DT.addColumn("Id");
        DT.addColumn("Nombre");
        DT.addColumn("Fecha");
        DT.addColumn("Stock");
        DT.addColumn("Categoria");
        DT.addColumn("Precio");
        DT.addColumn("Referencia");
        DT.addColumn("Peso");
        return DT;
    }
    
    public DefaultTableModel getDatosProductos(){
        try {
            setTitulosProductos();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_PRODUCTOS);
            RS = PS.executeQuery();
            Object[] fila = new Object[8];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);  
                fila[2] = RS.getString(3);  
                fila[3] = RS.getString(4);  
                fila[4] = RS.getString(5);  
                fila[5] = RS.getString(6);  
                fila[6] = RS.getString(7);  
                fila[7] = RS.getString(8);  
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los datos."+e.getMessage());
        } finally{
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return DT;
    }
    
    public int registrarProducto(String codigo, String descripcion, String fecha, String stock, String categoria, String precio, String referencia, String peso){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_PRODUCTOS);
            PS.setString(1, codigo);
            PS.setString(2, descripcion);
            PS.setString(3, fecha);
            PS.setString(4, stock);
            PS.setString(5, categoria);
            PS.setString(6, precio);
            PS.setString(7, referencia);
            PS.setString(8, peso);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el producto.");
            System.err.println("Error al registrar el producto." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
    public void insertarProductoInventario(String codigoProducto){
        int res;
        try {
            PS = CN.getConnection().prepareStatement("CALL NUEVO_PRODUCTO('"+codigoProducto+"')");
            PS.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro en la tabla inventario." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
    }
    
    public int verificarCodigoInventario(String codigo){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement("SELECT count(inv_pro_codigo) from inventario where inv_pro_codigo='"+codigo+"'");
            RS = PS.executeQuery();
            
            while(RS.next()){
                res = RS.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al devolver cantidad de registros." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
    
    public int actualizarProducto(String codigo, String descripcion,String fecha, String stock, String categoria, String precio, String referencia, String peso, String codigo_old){
        String SQL = "UPDATE producto SET pro_codigo='"+codigo+"',pro_descripcion='"+descripcion+"',pro_fecha='"+fecha+"',pro_stock='"+stock+"',pro_categoria='"+categoria+"',pro_precio='"+precio+"',pro_referencia='"+referencia+"',pro_peso='"+peso+"' WHERE pro_codigo='"+codigo_old+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar los datos del cliente." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
    public int eliminarProducto(String codigo){
        String SQL = "DELETE from producto WHERE pro_codigo ='"+codigo+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No es posible eliminar el producto.");
            System.err.println("Error al eliminar producto." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
}
