package ch.hearc.cafheg.infrastructure.persistance;

import ch.hearc.cafheg.business.allocations.Allocataire;
import ch.hearc.cafheg.business.allocations.NoAVS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllocataireMapper extends Mapper {

  private static final Logger logger = LoggerFactory.getLogger(AllocataireMapper.class);

  public List<Allocataire> findAll(String likeNom) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement;
      if (likeNom == null) {
        preparedStatement = connection
            .prepareStatement("SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES");
      } else {
        preparedStatement = connection
            .prepareStatement("SELECT NOM,PRENOM,NO_AVS FROM ALLOCATAIRES WHERE NOM LIKE ?");
        preparedStatement.setString(1, likeNom + "%");
      }
      List<Allocataire> allocataires = new ArrayList<>();
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          allocataires
              .add(new Allocataire(new NoAVS(resultSet.getString(3)), resultSet.getString(2),
                  resultSet.getString(1)));
        }
      }
      return allocataires;
    } catch (SQLException e) {
      logger.error("Unable to findAllAllocataire", e);
      throw new RuntimeException(e);
    }
  }

  public Allocataire findById(long id) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "SELECT NO_AVS, NOM, PRENOM FROM ALLOCATAIRES WHERE NUMERO=?");
      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();
      resultSet.next();
      return new Allocataire(new NoAVS(resultSet.getString(1)),
          resultSet.getString(2), resultSet.getString(3));
    } catch (SQLException e) {
      logger.error("Unable to findByIdAllocataire", e);
      throw new RuntimeException(e);
    }
  }

  public int removeAllocataireByID(long id) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "DELETE FROM ALLOCATAIRES WHERE NUMERO=?");
      preparedStatement.setLong(1, id);
      int nbRowDeleted = preparedStatement.executeUpdate();
      return nbRowDeleted;
    } catch (SQLException e) {
      logger.error("Unable to removeAllocataireByID", e);
      throw new RuntimeException(e);
    }
  }

  public int updateAllocataireNomPrenom(long id, String nomAllocataire, String prenomAllocataire) {
    Connection connection = getConnection();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(
          //UPDATE table_name
          //SET column1 = value1, column2 = value2, ...
          //WHERE condition;
          "UPDATE ALLOCATAIRES SET NOM=?, PRENOM=? WHERE NUMERO=?");
      preparedStatement.setString(1, nomAllocataire);
      preparedStatement.setString(2, prenomAllocataire);
      preparedStatement.setLong(3, id);
      int nbRowUpdated = preparedStatement.executeUpdate();
      return nbRowUpdated;
    } catch (SQLException e) {
      logger.error("Unable to updateAllocataireNomPrenom", e);
      throw new RuntimeException(e);
    }
  }
}
