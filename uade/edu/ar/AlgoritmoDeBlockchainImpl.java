package uade.edu.ar;

import uade.edu.progra3.AlgoritmoDeBlockchain;
import uade.edu.progra3.model.Bloque;
import uade.edu.progra3.model.Transaccion;

import java.util.ArrayList;
import java.util.List;

public class AlgoritmoDeBlockchainImpl implements AlgoritmoDeBlockchain {

    @Override
    public List<List<Bloque>> construirBlockchain(List<Transaccion> transacciones, int maxTamanioBloque, int maxValorBloque, int maxTransacciones, int maxBloques) {
        List<List<Bloque>> soluciones = new ArrayList<>();
        construirBlockchainBacktracking(transacciones, 0, new ArrayList<>(), soluciones, maxTamanioBloque, maxValorBloque, maxTransacciones, maxBloques);
        return soluciones;
    }

    private void construirBlockchainBacktracking(
            List<Transaccion> transacciones,
            int index,
            List<Bloque> bloquesActuales,
            List<List<Bloque>> soluciones,
            int maxTamanioBloque,
            int maxValorBloque,
            int maxTransacciones,
            int maxBloques) {

        // Caso base: todas las transacciones fueron asignadas
        if (index == transacciones.size()) {
            // Verificar prueba de trabajo y guardar los bloques actuales
            if (cumplenPruebaDeTrabajo(bloquesActuales)) {
                soluciones.add(copiarBloques(bloquesActuales));
            }
            return;
        }

        Transaccion transaccion = transacciones.get(index);

        // La transacción se firma si es necesario
        while (!transaccion.isFirmada()) {
            transaccion.agregarFirma();
        }

        List<Bloque> bloquesExistentes = new ArrayList<>(bloquesActuales);
        for (Bloque bloque : bloquesExistentes) {
            if (cumpleRestricciones(bloque, transaccion, maxTamanioBloque, maxValorBloque, maxTransacciones)) {
                // Guardamos el estado actual para restaurarlo
                bloque.getTransacciones().add(transaccion);
                bloque.setTamanioTotal(bloque.getTamanioTotal() + transaccion.getTamanio());
                bloque.setValorTotal(bloque.getValorTotal() + transaccion.getValor());

                // Llamada recursiva al siguiente nivel
                construirBlockchainBacktracking(transacciones, index + 1, bloquesActuales, soluciones, maxTamanioBloque, maxValorBloque, maxTransacciones, maxBloques);

                // Deshacemos los cambios
                bloque.getTransacciones().removeLast();
                bloque.setTamanioTotal(bloque.getTamanioTotal() - transaccion.getTamanio());
                bloque.setValorTotal(bloque.getValorTotal() - transaccion.getValor());
            }
        }

        // Intenta crear un nuevo bloque si no se alcanzo el máximo de bloques
        if (bloquesActuales.size() < maxBloques && transaccion.getDependencia() == null) {
            Bloque nuevoBloque = new Bloque();
            nuevoBloque.getTransacciones().add(transaccion);
            nuevoBloque.setTamanioTotal(transaccion.getTamanio());
            nuevoBloque.setValorTotal(transaccion.getValor());
            bloquesActuales.add(nuevoBloque);

            // Llamada recursiva
            construirBlockchainBacktracking(transacciones, index + 1, bloquesActuales, soluciones, maxTamanioBloque, maxValorBloque, maxTransacciones, maxBloques);

            // Deshacemos los cambios
            bloquesActuales.removeLast();
        }
    }

    private boolean cumpleRestricciones(Bloque bloque, Transaccion transaccion, int maxTamanioBloque, int maxValorBloque, int maxTransacciones) {
        return (
                bloque.getTamanioTotal() + transaccion.getTamanio() <= maxTamanioBloque &&
                        bloque.getValorTotal() + transaccion.getValor() <= maxValorBloque &&
                        bloque.getTransacciones().size() + 1 <= maxTransacciones &&
                        (transaccion.getDependencia() == null || bloque.getTransacciones().contains(transaccion.getDependencia()))
        );
    }

    private boolean cumplenPruebaDeTrabajo(List<Bloque> bloques) {
        for (Bloque bloque : bloques) {
            if (bloque.getValorTotal() % 10 != 0) {
                return false;
            }
        }
        return true;
    }

    private List<Bloque> copiarBloques(List<Bloque> bloques) {
        List<Bloque> copia = new ArrayList<>();
        for (Bloque bloque : bloques) {
            Bloque copiaBloque = new Bloque();
            copiaBloque.setTamanioTotal(bloque.getTamanioTotal());
            copiaBloque.setValorTotal(bloque.getValorTotal());

            List<Transaccion> transaccionesCopia = new ArrayList<>(bloque.getTransacciones());
            copiaBloque.setTransacciones(transaccionesCopia);
            copia.add(copiaBloque);
        }
        return copia;
    }
}