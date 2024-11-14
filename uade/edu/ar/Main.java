package uade.edu.ar;

import uade.edu.progra3.model.Transaccion;
import uade.edu.progra3.model.Bloque;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AlgoritmoDeBlockchainImpl algoritmo = new AlgoritmoDeBlockchainImpl();

        // Restricciones de prueba
        int maxTamanioBloque = 50;       // Tamaño máximo del bloque en KB
        int maxValorBloque = 50;        // Valor máximo del bloque
        int maxTransacciones = 2;        // Número máximo de transacciones por bloque
        int maxBloques = 3;              // Número máximo de bloques

        List<Transaccion> transacciones = new ArrayList<>();

        Transaccion t1 = new Transaccion(10, 10, null, 0);
        Transaccion t2 = new Transaccion(15, 15, null, 0);
        Transaccion t3 = new Transaccion(20, 15, null, 0);
        transacciones.add(t1);
        transacciones.add(t2);
        transacciones.add(t3);

        List<List<Bloque>> soluciones = algoritmo.construirBlockchain(transacciones, maxTamanioBloque, maxValorBloque, maxTransacciones, maxBloques);
        imprimirSoluciones(soluciones);
    }

    // Para imprimir las soluciones generadas
    private static void imprimirSoluciones(List<List<Bloque>> soluciones) {
        System.out.println("Número de soluciones encontradas: " + soluciones.size());
        int i = 1;
        for (List<Bloque> blockchain : soluciones) {
            System.out.println("Solución " + i + ":");
            int j = 1;
            for (Bloque bloque : blockchain) {
                System.out.println("  Bloque " + j + ":");
                System.out.println("    Tamaño total: " + bloque.getTamanioTotal() + " KB");
                System.out.println("    Valor total: " + bloque.getValorTotal());
                System.out.println("    Transacciones: ");
                for (Transaccion transaccion : bloque.getTransacciones()) {
                    System.out.println("      - Tamaño: " + transaccion.getTamanio() + " KB, Valor: " + transaccion.getValor());
                }
                j++;
            }
            i++;
        }
    }
}
