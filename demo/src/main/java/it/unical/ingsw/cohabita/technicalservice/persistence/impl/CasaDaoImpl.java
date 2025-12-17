package it.unical.ingsw.cohabita.technicalservice.persistence.impl;

import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.CasaDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.db.DBConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CasaDaoImpl implements CasaDao{

    private final Connection conn;

    public CasaDaoImpl() {
        this.conn = DBConnectionManager.getInstance().getConnection();
    }


    private static final String trovaCasaConInvito=
            "SELECT id_casa, nome_casa, codice_invito FROM abitazioni WHERE codice_invito = ?";

    private static final String inserisciCasa=
            "INSERT INTO abitazioni (nome_casa, codice_invito) VALUES (?, ?) RETURNING id_casa, nome_casa, codice_invito";

    private static final String trovaId=
            "SELECT id_casa, nome_casa, codice_invito FROM abitazioni WHERE id_casa = ?";



    @Override
    public Casa trovaCasaInvito(String codiceInvito) {
        try (PreparedStatement ps = conn.prepareStatement(trovaCasaConInvito)) {

            ps.setString(1, codiceInvito);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return creaIstanzaCasa(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public Casa creaCasa(String nomeCasa, String codiceInvito) {
        try (PreparedStatement ps = conn.prepareStatement(inserisciCasa)) {
            ps.setString(1, nomeCasa);
            ps.setString(2, codiceInvito);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return creaIstanzaCasa(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Casa trovaCasaId(Integer idCasa) {

        try (PreparedStatement ps = conn.prepareStatement(trovaId)) {
            ps.setInt(1, idCasa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return creaIstanzaCasa(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }






    private Casa creaIstanzaCasa (ResultSet rs) throws SQLException {
        Integer idCasa = rs.getInt("id_casa");
        String nomeCasa = rs.getString("nome_casa");
        String codiceInvito = rs.getString("codice_invito");
        return new Casa(idCasa, nomeCasa, codiceInvito);
    }


}



