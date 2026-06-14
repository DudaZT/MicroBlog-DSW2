package edu.ifsp.microblog.util;

// Encapsula dados de paginação para ser enviado às views
public class Paginacao {

    private final int paginaAtual;
    private final int totalPaginas;
    private final int tamanhoPagina;
    private final long totalItens;

    public Paginacao(int paginaAtual, long totalItens, int tamanhoPagina) {
        this.paginaAtual   = Math.max(1, paginaAtual);
        this.totalItens    = totalItens;
        this.tamanhoPagina = tamanhoPagina;
        this.totalPaginas  = (int) Math.ceil((double) totalItens / tamanhoPagina);
    }

    public int getPaginaAtual()   { return paginaAtual; }
    public int getTotalPaginas()  { return totalPaginas; }
    public int getTamanhoPagina() { return tamanhoPagina; }
    public long getTotalItens()   { return totalItens; }

    public boolean temProxima()   { return paginaAtual < totalPaginas; }
    public boolean temAnterior()  { return paginaAtual > 1; }
    public int proxima()          { return paginaAtual + 1; }
    public int anterior()         { return paginaAtual - 1; }


    public static int parsePage(String param) {
        try {
            int p = Integer.parseInt(param);
            
            return p < 1 ? 1 : p;
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}