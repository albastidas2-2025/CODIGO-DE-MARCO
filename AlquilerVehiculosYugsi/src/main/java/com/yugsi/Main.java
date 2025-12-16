package com.yugsi;
import com.yugsi.gui.VistaPrincipal;
import javax.swing.SwingUtilities;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
        public static void main(String[] args) {
            // Establecer el Look and Feel del sistema
            try {
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Ejecutar la aplicación en el Event Dispatch Thread
            SwingUtilities.invokeLater(() -> {
                VistaPrincipal ventana = new VistaPrincipal();
                ventana.setVisible(true);
            });
        }
    }