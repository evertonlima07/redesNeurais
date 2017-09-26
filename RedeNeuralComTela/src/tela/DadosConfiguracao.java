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
public class DadosConfiguracao {
    
    int numEpocas, numCamadas;
    double erroAceito, taxaAprendizado;
    
    public DadosConfiguracao(){
    }

    public double getErroAceito() {
        return erroAceito;
    }

    public int getNumCamadas() {
        return numCamadas;
    }

    public int getNumEpocas() {
        return numEpocas;
    }

    public double getTaxaAprendizado() {
        return taxaAprendizado;
    }

    public void setErroAceito(double erroAceito) {
        this.erroAceito = erroAceito;
    }

    public void setNumCamadas(int numCamadas) {
        this.numCamadas = numCamadas;
    }

    public void setNumEpocas(int numEpocas) {
        this.numEpocas = numEpocas;
    }

    public void setTaxaAprendizado(double taxaAprendizado) {
        this.taxaAprendizado = taxaAprendizado;
    }
}
