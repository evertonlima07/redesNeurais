/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tela;

/**
 *
 * @author evert
 */
public class Home {

    public static void main(String[] args) {
        TelaPrincipal tlPrincipal;

        tlPrincipal = new TelaPrincipal();
        tlPrincipal.setLocationRelativeTo(null);
        tlPrincipal.setResizable(false);
        tlPrincipal.setTitle("Diagnóstico Rápido Mamografia");
        
        RedeNeural.telaPrincipal = tlPrincipal;
        Configuracao.telaPrincipal = tlPrincipal;
        
        tlPrincipal.setVisible(true);
    }
}
