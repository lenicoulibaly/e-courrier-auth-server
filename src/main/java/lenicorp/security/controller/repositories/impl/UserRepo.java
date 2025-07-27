package lenicorp.security.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import lenicorp.security.controller.repositories.spec.IUserRepo;
import lenicorp.security.model.dtos.UserDTO;
import lenicorp.security.model.entities.AppUser;
import lenicorp.structures.controller.repositories.VStrRepo;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class UserRepo implements IUserRepo
{
    private final VStrRepo vStrRepo;
    public AppUser findByUsername(String username)
    {
        return find("email", username).firstResult();
    }

    public boolean existsById(Long userId)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE u.userId = ?1", userId) > 0;
    }

    public boolean existsByEmail(String email)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE UPPER(u.email) = upper(?1)", email) > 0;
    }

    public boolean existsByEmail(String email,  Long userId)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE UPPER(u.email) = upper(?1) AND u.userId <> ?2", email, userId) > 0;
    }

    public Page<UserDTO> searchUsers(String key, Long strId, PageRequest pageRequest)
    {
        String baseQuery = """
            FROM app_user u
            LEFT JOIN v_structure str ON str.str_id = u.str_id
            WHERE 
            (
                unaccent(UPPER(u.email)) LIKE :key
                OR unaccent(UPPER(u.first_name)) LIKE :key
                OR unaccent(UPPER(u.last_name)) LIKE :key
                OR unaccent(UPPER(u.tel)) LIKE :key
            )
            """;

        String selectQuery = """
            SELECT u.user_id, u.email, u.first_name, u.last_name, u.tel, 
                   u.change_password_date, u.activated, u.not_blocked, u.last_login,
                   str.str_id, str.str_name, str.str_sigle, str.chaine_sigles
            """ + baseQuery;
        String countQuery = "SELECT COUNT(*) " + baseQuery;


        var chaineSigles = strId != null ? vStrRepo.getChaineSigles(strId) : null;

        if(strId != null && chaineSigles != null)
        {
            selectQuery += " AND str.chaine_sigles LIKE :chaineSigles";
            countQuery += " AND str.chaine_sigles LIKE :chaineSigles";
        }

        selectQuery = selectQuery + " ORDER BY u.last_name, u.first_name";

        key = "%" +StringUtils.stripAccentsToUpperCase(key) + "%";

        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery).setParameter("key", key);
        if(strId != null && chaineSigles != null)
        {
            countQueryExecutor.setParameter("chaineSigles", chaineSigles + "%");
        }
        Long count = ((Number) countQueryExecutor.getSingleResult()).longValue();

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery)
                .setParameter("key", key)
                .setFirstResult(pageRequest.getPage() * pageRequest.getSize())
                .setMaxResults(pageRequest.getSize());
        if(strId != null && chaineSigles != null)
        {
            selectQueryExecutor.setParameter("chaineSigles", chaineSigles + "%");
        }

        List<Object[]> results = selectQueryExecutor.getResultList();
        List<UserDTO> content = results.stream()
                .map(this::mapToUserDTO)
                .collect(java.util.stream.Collectors.toList());

        return new Page<>(content, count, pageRequest.getPage(), pageRequest.getSize());
    }

    public boolean existsByTel(String tel, Long userId)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE UPPER(u.tel) = upper(?1) AND u.userId <> ?2", tel, userId) > 0;
    }

    public boolean existsByTel(String tel)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE UPPER(u.tel) = upper(?1)", tel) > 0;
    }

    @Override
    public boolean userIsActive(Long userId)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE u.userId = ?1 AND u.activated", userId) > 0;
    }

    @Override
    public boolean userIsActive(String username)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE u.email = ?1 AND u.activated", username) > 0;
    }

    @Override
    public boolean userIsNotBlocked(Long userId)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE u.userId = ?1 AND u.notBlocked", userId) > 0;
    }

    @Override
    public boolean userIsNotBlocked(String username)
    {
        return count("SELECT COUNT(u) FROM AppUser u WHERE u.email = ?1 AND u.notBlocked", username) > 0;
    }

    @Override
    public String getPasswordByUsername(String username)
    {
        return getEntityManager()
                .createQuery("SELECT u.password FROM AppUser u WHERE u.email = ?1", String.class)
                .setParameter(1, username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<UserDTO> getUsersByStructure(Long strId)
    {
        if (strId == null) {
            return List.of();
        }

        String baseQuery = """
            FROM app_user u
            LEFT JOIN v_structure str ON str.str_id = u.str_id
            WHERE 1=1
            """;

        String selectQuery = """
            SELECT u.user_id, u.email, u.first_name, u.last_name, u.tel, 
                   u.change_password_date, u.activated, u.not_blocked, u.last_login,
                   str.str_id, str.str_name, str.str_sigle, str.chaine_sigles
            """ + baseQuery;

        var chaineSigles = vStrRepo.getChaineSigles(strId);

        if (chaineSigles != null) {
            selectQuery += " AND str.chaine_sigles LIKE :chaineSigles";
        } else {
            return List.of();
        }

        selectQuery = selectQuery + " ORDER BY u.last_name, u.first_name";

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        selectQueryExecutor.setParameter("chaineSigles", chaineSigles + "%");

        List<Object[]> results = selectQueryExecutor.getResultList();
        return results.stream()
                .map(this::mapToUserDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    private UserDTO mapToUserDTO(Object[] row)
    {
        return new UserDTO(
                (Long) row[0],           // userId
                (String) row[1],         // email
                (String) row[2],         // firstName
                (String) row[3],         // lastName
                (String) row[4],         // tel
                row[5] != null ? ((java.sql.Date) row[5]).toLocalDate() : null, // changePasswordDate
                (Boolean) row[6],        // activated
                (Boolean) row[7],        // notBlocked
                row[8] != null ? ((java.sql.Timestamp) row[8]).toLocalDateTime() : null, // lastLogin
                (Long) row[9],           // strId
                (String) row[10],         // strName
                (String) row[11],        // strSigle
                (String) row[12]         // chaineSigles
        );
    }


}
