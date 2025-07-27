package lenicorp.security.controller.repositories.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lenicorp.security.controller.repositories.spec.IAuthAssoRepo;
import lenicorp.security.model.dtos.AuthorityDTO;
import lenicorp.security.model.dtos.UserProfileAssoDTO;
import lenicorp.security.model.entities.AppAuthority;
import lenicorp.security.model.entities.AppUser;
import lenicorp.security.model.mappers.AuthorityMapper;
import lenicorp.security.model.mappers.UserMapper;
import lenicorp.security.model.views.VProfile;
import lenicorp.security.model.views.VUserProfile;
import lenicorp.structures.controller.repositories.VStrRepo;
import lenicorp.utilities.Page;
import lenicorp.utilities.PageRequest;
import lenicorp.utilities.StringUtils;
import lombok.RequiredArgsConstructor;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped @RequiredArgsConstructor
public class AuthAssoRepo implements IAuthAssoRepo
{
    private final AuthorityMapper authorityMapper;
    private final VStrRepo vStrRepo;
    private final VProfileRepo vProfileRepo;
    private final UserRepo userRepo;
    private final UserMapper userMapper;

    @Override
    public VUserProfile findUserCurrentProfile(String username)
    {
        String sql = "select vp from VUserProfile vp where vp.email = :username and vp.assStatusCode = 'STA_ASS_CUR'";
        VUserProfile profile = getEntityManager()
                .createQuery(sql, VUserProfile.class)
                .setParameter("username", username)
                .getResultList().stream().findFirst().orElse(null);

        if(profile == null)
        {
            VProfile userProfile = vProfileRepo.findById("PRFL_USR");
            AppUser user = userRepo.findByUsername(username);
            VUserProfile vUserProfile = userMapper.mapToVUserProfile(user, userProfile) ;
            return vUserProfile;
        }
        return profile;
    }

    @Override
    public List<AuthorityDTO> findRolePrivileges(String roleCode)
    {
        if(roleCode == null) return List.of();
        String sql = "select ass.privilege from AuthAssociation ass where ass.role.code = :roleCode and ass.type.code = 'ROL_PRV'";
        List<AuthorityDTO> privileges = getEntityManager().createQuery(sql, AppAuthority.class)
                .setParameter("roleCode", roleCode)
                .getResultList().stream().map(authorityMapper::mapToAuthorityDTO).toList();
        return privileges;
    }

    @Override
    public List<AuthorityDTO> findProfileRoles(String profileCode)
    {
        if(profileCode == null) return List.of();
        String sql = "select ass.role from AuthAssociation ass where ass.profile.code = :profileCode and ass.type.code = 'PRFL_ROL'";
        List<AuthorityDTO> roles = getEntityManager().createQuery(sql, AppAuthority.class)
                .setParameter("profileCode", profileCode)
                .getResultList().stream().map(authorityMapper::mapToAuthorityDTO).toList();
        return roles;
    }

    @Override
    public Set<String> findAuthoritiesByRoleCode(String roleCode)
    {
        List<AuthorityDTO> privileges = findRolePrivileges(roleCode);
        if(privileges == null || privileges.isEmpty()) return Set.of();
        return privileges.stream().map(AuthorityDTO::getCode).collect(Collectors.toSet());
    }


    @Override
    public Set<String> findAuthoritiesByProfileCode(String profileCode)
    {
        if(profileCode == null) return Set.of();

        String sql = """
        select distinct vpp.privilege_code as authority_code
        from v_profile_privilege vpp 
        where vpp.profile_code = ?1
        union
        select distinct vpr.role_code as authority_code
        from v_profile_role vpr 
        where vpr.profile_code = ?1
        """;

        List<String> authorities = getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, profileCode)
                .getResultList();

        return Set.copyOf(authorities);
    }

    @Override
    public Set<String> findAuthoritiesByUsername(String username)
    {
        VUserProfile profile = findUserCurrentProfile(username);
        if(profile == null) return Set.of();
        return this.findAuthoritiesByProfileCode(profile.getProfileCode());
    }

    @Override
    public boolean existsByRoleCodeAndPrivilegeCode(String roleCode, String privilegeCode)
    {
        return count("role.code = ?1 and privilege.code = ?2", roleCode, privilegeCode) > 0;
    }

    @Override
    public boolean existsByProfileCodeAndRoleCode(String profileCode, String roleCode)
    {
        return count("profile.code = ?1 and role.code = ?2", profileCode, roleCode) > 0;
    }

    @Override /**Les privilèges dont le code est présent dans le paramètre 2 que le role du paramètre 1 ne possède pas*/
    public List<AuthorityDTO> findPrivilesToAddOnRole(String roleCode, List<String> privilegeCodes)
    {
        if(privilegeCodes == null || privilegeCodes.isEmpty()) return List.of();
        String sql = """
                SELECT DISTINCT vp.code, vp.name, vp.description, 
                'PRV' as type_code, 'Privilège' as type_name, vp.privilege_type_code, vp.privilege_type_name  
                FROM v_privilege vp 

                WHERE 
                    vp.code IN (?2) 
                    AND vp.code NOT IN 
                        (SELECT vrp.privilege_code FROM v_role_privilege vrp WHERE vrp.role_code = ?1)
                """;
        return getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, roleCode)
                .setParameter(2, privilegeCodes)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer((tuple, aliases) -> {
                    String code = (String) tuple[0];
                    String name = (String) tuple[1];
                    String description = (String) tuple[2];
                    String typeCode = (String) tuple[3];
                    String typeName = (String) tuple[4];
                    String privilegeTypeCode = (String) tuple[5];
                    String privilegeTypeName = (String) tuple[6];
                    return new AuthorityDTO(code, name, description, typeCode, typeName, privilegeTypeCode, privilegeTypeName);
                })
                .getResultList();
    }

    @Override /**Les privilèges que le role du paramètre (roleCode) 1 possède dont le code n'est pas dans le paramètre 2 (privilegeCodes) */
    public List<AuthorityDTO> findPrivilesToRemoveOnRole(String roleCode, List<String> privilegeCodes)
    {
        if(privilegeCodes == null || privilegeCodes.isEmpty()) return List.of();
        String sql = """
                SELECT DISTINCT vp.code, vp.name, vp.description, 
                'PRV' as type_code, 'Privilège' as type_name, vp.privilege_type_code, vp.privilege_type_name
                FROM v_privilege vp 
                WHERE 
                    vp.code NOT IN (?2) 
                    AND vp.code IN (select vrp.privilege_code from v_role_privilege vrp where vrp.role_code = ?1)
        """;
        return getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, roleCode)
                .setParameter(2, privilegeCodes)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer((tuple, aliases) -> {
                    String code = (String) tuple[0];
                    String name = (String) tuple[1];
                    String description = (String) tuple[2];
                    String typeCode = (String) tuple[3];
                    String typeName = (String) tuple[4];
                    String privilegeTypeCode = (String) tuple[5];
                    String privilegeTypeName = (String) tuple[6];
                    return new AuthorityDTO(code, name, description, typeCode, typeName, privilegeTypeCode, privilegeTypeName);
                })
                .getResultList();
    }

    /**Supprimer les enregistrements de auth_association dont le role_code = ?1 et privilege_code = ?2 et type_code = 'ROL_PRV'*/
    @Override @Transactional
    public void removePrivilegeToRole(String roleCode, String privilegeCode)
    {
        if (roleCode == null || privilegeCode == null) return;
        String sql = """
                DELETE FROM auth_association 
                WHERE role_code = ?1 
                AND privilege_code = ?2 
                AND type_code = 'ROL_PRV'
        """;
        getEntityManager().createNativeQuery(sql)
                .setParameter(1, roleCode)
                .setParameter(2, privilegeCode)
                .executeUpdate();
        ;
    }

    @Override /**Les roles dont le code est présent dans le paramètre 2 (roleCodes) que le profile du paramètre 1 (profileCode) ne possède pas*/
    public List<AuthorityDTO> findRolesToAddOnProfile(String profileCode, List<String> roleCodes)
    {
        if(roleCodes == null || roleCodes.isEmpty()) return List.of();
        String sql = """
                SELECT DISTINCT rol.code as code, rol.name as name, rol.description as description, 
                t.code as type_code, t.name as type_name 
                FROM app_authority rol 
                    LEFT JOIN type t ON t.code = rol.type_code 
                WHERE 
                    rol.code IN (?2) 
                    AND rol.code NOT IN (select vpr.role_code from v_profile_role vpr where vpr.profile_code = ?1)
        """;
        return getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, profileCode)
                .setParameter(2, roleCodes)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(AuthorityDTO.class))
                .getResultList();
    }

    @Override /**Les role que le profile du paramètre (profileCode) 1 possède dont le code n'est pas dans le paramètre 2 (roleCodes) */
    public List<AuthorityDTO> findRolesToRemoveOnProfile(String profileCode, List<String> roleCodes)
    {
        if(roleCodes == null || roleCodes.isEmpty()) return List.of();
        String sql = """
                select DISTINCT rol.code as code, rol.name as name, rol.description as description, 
                t.code as type_code, t.name as type_name from app_authority rol 
                left join type t on t.code = rol.type_code 
                where t.code = 'ROL' 
                and rol.code in (select vpr.role_code from v_profile_role vpr where vpr.profile_code = ?1)
                and rol.code NOT IN (?2) 
                """;
        return getEntityManager()
                .createNativeQuery(sql)
                .setParameter(1, profileCode)
                .setParameter(2, roleCodes)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new AliasToBeanResultTransformer(AuthorityDTO.class))
                .getResultList();
    }

    /**Supprimer les enregistrements de auth_association dont le profile_code = ?1 et role_code = ?2 et type_code = 'PRFL_ROL'*/
    @Override @Transactional
    public void removeRoleToProfile(String profileCode, String roleCode)
    {
        if (profileCode == null || roleCode == null) return;
        String sql = """
                DELETE FROM auth_association 
                WHERE profile_code = ?1 
                AND role_code = ?2 
                AND type_code = 'PRFL_ROL'
        """;
        getEntityManager().createNativeQuery(sql)
                .setParameter(1, profileCode)
                .setParameter(2, roleCode)
                .executeUpdate();
    }

    @Override
    public boolean existsByUserIdAndProfileCodeAndStrId(Long userId, String profileCode, Long strId)
    {
        if(userId == null || profileCode == null || strId == null) return false;
        String sql = """
        select count(vup) 
        from VUserProfile vup 
        where vup.userId = :userId and vup.profileCode = :profileCode and vup.profileStrId = :strId
        """;
        Long count = this.getEntityManager().createQuery(sql, Long.class)
                .setParameter("userId", userId)
                .setParameter("profileCode", profileCode)
                .setParameter("strId", strId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean userHasAnyProfile(Long userId)
    {
        if(userId == null) return false;
        String sql = "select count(vup.userId) from VUserProfile vup where vup.userId = :userId and vup.assStatusCode <> 'STA_ASS_INACT'";
        Long count = this.getEntityManager().createQuery(sql, Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public Page<AuthorityDTO> searchPrivileges(String key, List<String> privilegeTypeCodes, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        key = "%" + StringUtils.stripAccentsToUpperCase( key ) + "%";
        String baseQuery = """
    from v_privilege vp 
    where (upper(unaccent(vp.name)) like :key or upper(unaccent(vp.code)) like :key or upper(unaccent(vp.description)) like :key) 
    """;
        String selectQuery = "select vp.code, vp.name, vp.description, vp.privilege_type_code, vp.privilege_type_name " + baseQuery ;
        String countQuery = "select count(*) " + baseQuery;
        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty())
        {
            selectQuery += " and vp.privilege_type_code in (:privilegeTypeCodes)";
            countQuery += " and vp.privilege_type_code in (:privilegeTypeCodes) ";
        }
        selectQuery += "   order by vp.name";

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery, Long.class);
        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty())
        {
            selectQueryExecutor.setParameter("privilegeTypeCodes", privilegeTypeCodes);
            countQueryExecutor.setParameter("privilegeTypeCodes", privilegeTypeCodes);
        }

        Long count = (Long) countQueryExecutor.setParameter("key", key).getSingleResult();

        List<Object[]> results = selectQueryExecutor.setParameter("key", key)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // Utilisation du constructeur spécialisé
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        "PRV", //typeCode
                        "Privilège",
                        (String) row[3],  // privilegeTypeCode
                        (String) row[4],
                        "PRV"
                ))
                .collect(Collectors.toList());

        return new Page<>(content, count, page, size);
    }

    @Override
    public List<AuthorityDTO> getPrivilegesListByTypeCode(List<String> privilegeTypeCodes)
    {
        String sql = """
    select vp.code, vp.name, vp.description, vp.privilege_type_code, vp.privilege_type_name
    from v_privilege vp 
    where true
    """;

        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sql += " and vp.privilege_type_code in (:privilegeTypeCodes)";
        sql += "   order by vp.name";

        var sqlExecutor = getEntityManager().createNativeQuery(sql);
        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sqlExecutor.setParameter("privilegeTypeCodes", privilegeTypeCodes);

        List<Object[]> results = sqlExecutor.getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRV"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        "PRV", // typeCode
                        "Privilège", // typeName
                        (String) row[3], // privilegeTypeCode
                        (String) row[4], // privilegeTypeName
                        "PRV" // authType pour assigner privilegeCode
                ))
                .collect(Collectors.toList());
        return content;
    }

    @Override
    public List<AuthorityDTO> getPrivilegesListByRoleCodes(List<String> roleCodes)
    {
        String sql = """
    select distinct vp.code, vp.name, vp.description, vp.privilege_type_code, vp.privilege_type_name
    from v_privilege vp 
    where true
    """;

        if(roleCodes != null && !roleCodes.isEmpty()) {
            sql += """
            and vp.code in (
                select vrp.privilege_code 
                from v_role_privilege vrp 
                where vrp.role_code in (:roleCodes)
            )
            """;
        }
        sql += "   order by vp.name";

        var sqlExecutor = getEntityManager().createNativeQuery(sql);
        if(roleCodes != null && !roleCodes.isEmpty()) sqlExecutor.setParameter("roleCodes", roleCodes);

        List<Object[]> results = sqlExecutor.getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRV"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        "PRV", // typeCode
                        "Privilège", // typeName
                        (String) row[3], // privilegeTypeCode
                        (String) row[4], // privilegeTypeName
                        "PRV" // authType pour assigner privilegeCode
                ))
                .collect(Collectors.toList());
        return content;
    }

    @Override
    public List<AuthorityDTO> searchPrivilegesByRoleCode(String roleCode, String key, List<String> privilegeTypeCodes)
    {
        key = "%" + StringUtils.stripAccentsToUpperCase( key ) + "%";
        String sql = """
    select vp.code, vp.name, vp.description, vp.privilege_type_code, vp.privilege_type_name
    from v_privilege vp 
    where (upper(unaccent(vp.name)) like :key or upper(unaccent(vp.code)) like :key or upper(unaccent(vp.description)) like :key)
    and (:roleCode is null or vp.code in (select vrp.privilege_code from v_role_privilege vrp where vrp.role_code = :roleCode))
    """;

        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sql += " and vp.privilege_type_code in (:privilegeTypeCodes)";
        sql += "   order by vp.name";

        var sqlExecutor = getEntityManager().createNativeQuery(sql);
        sqlExecutor.setParameter("roleCode", roleCode).setParameter("key", key);
        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sqlExecutor.setParameter("privilegeTypeCodes", privilegeTypeCodes);

        List<Object[]> results = sqlExecutor.getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRV"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        "PRV", // typeCode
                        "Privilège", // typeName
                        (String) row[3], // privilegeTypeCode
                        (String) row[4], // privilegeTypeName
                        "PRV" // authType pour assigner privilegeCode
                ))
                .collect(Collectors.toList());

        return content;
    }

    @Override
    public List<AuthorityDTO> searchPrivilegesByProfileCode(String profileCode, String key, List<String> privilegeTypeCodes)
    {
        key = "%" + StringUtils.stripAccentsToUpperCase( key ) + "%";
        String sql = """
        select vpp.privilege_code, vpp.privilege_name, vpp.privilege_description, vpp.privilege_type_code, vpp.privilege_type_name
        from v_profile_privilege vpp 
        where (upper(unaccent(vpp.privilege_name)) like :key or upper(unaccent(vpp.privilege_code)) like :key or upper(unaccent(vpp.privilege_description)) like :key)
        and (vpp.profile_code = coalesce(:profileCode, vpp.profile_code)) 
        """;

        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sql += " and vpp.privilege_type_code in (:privilegeTypeCodes)";
        sql += "   order by vpp.privilege_name";

        var sqlExecutor = getEntityManager().createNativeQuery(sql);
        sqlExecutor.setParameter("profileCode", profileCode).setParameter("key", key);
        if(privilegeTypeCodes != null && !privilegeTypeCodes.isEmpty()) sqlExecutor.setParameter("privilegeTypeCodes", privilegeTypeCodes);

        List<Object[]> results = sqlExecutor.getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRV"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // privilege_code
                        (String) row[1], // privilege_name
                        (String) row[2], // privilege_description
                        "PRV", // typeCode
                        "Privilège", // typeName
                        (String) row[3], // privilege_type_code
                        (String) row[4], // privilege_type_name
                        "PRV" // authType pour assigner privilegeCode
                ))
                .collect(Collectors.toList());

        return content;
    }


    @Override
    public Page<AuthorityDTO> searchRoles(String key, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        key = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";

        String baseQuery = """
    from v_role vr  
    where (upper(unaccent(vr.name)) like ?1 or upper(unaccent(vr.code)) like ?1 or upper(unaccent(vr.description)) like ?1)
    """;

        String selectQuery = "select vr.code, vr.name, vr.description " + baseQuery + " order by vr.name";
        String countQuery = "select count(*) " + baseQuery;

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery, Long.class);

        Long count = (Long) countQueryExecutor.setParameter(1, key).getSingleResult();

        List<Object[]> results = selectQueryExecutor.setParameter(1, key)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // Utilisation du constructeur spécialisé avec authType "ROL"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        "ROL", // typeCode
                        "Rôle", // typeName
                        "ROL" // authType pour assigner roleCode
                ))
                .collect(Collectors.toList());

        return new Page<>(content, count, page, size);
    }


    @Override
    public List<AuthorityDTO> searchRolesByProfileCode(String profileCode, String key)
    {
        key = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";

        String sql = """
    select vpr.role_code, vpr.role_name, vpr.role_description
    from v_profile_role vpr 
    where (upper(unaccent(vpr.role_name)) like ?2 or upper(unaccent(vpr.role_code)) like ?2 or upper(unaccent(vpr.role_description)) like ?2)
    and (vpr.profile_code = coalesce(?1, vpr.profile_code))
    order by vpr.role_name
    """;

        var sqlExecutor = getEntityManager().createNativeQuery(sql);
        sqlExecutor.setParameter(1, profileCode).setParameter(2, key);

        List<Object[]> results = sqlExecutor.getResultList();

        // Utilisation du constructeur spécialisé avec authType "ROL"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // role_code
                        (String) row[1], // role_name
                        (String) row[2], // role_description
                        "ROL", // typeCode
                        "Rôle", // typeName
                        "ROL" // authType pour assigner roleCode
                ))
                .collect(Collectors.toList());

        return content;
    }

    @Override
    public Page<AuthorityDTO> searchProfiles(String key, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        key = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";

        String baseQuery = """
    from v_profile vp 
    where (upper(unaccent(vp.name)) like ?1 or upper(unaccent(vp.code)) like ?1 or upper(unaccent(vp.description)) like ?1)
    """;

        String selectQuery = "select vp.code, vp.name, vp.description, vp.type_code " + baseQuery + " order by vp.name";
        String countQuery = "select count(*) " + baseQuery;

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery, Long.class);

        Long count = (Long) countQueryExecutor.setParameter(1, key).getSingleResult();

        List<Object[]> results = selectQueryExecutor.setParameter(1, key)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRFL"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // code
                        (String) row[1], // name
                        (String) row[2], // description
                        (String) row[3], // type_code
                        "Profil", // typeName
                        "PRFL" // authType pour assigner profileCode
                ))
                .collect(Collectors.toList());

        return new Page<>(content, count, page, size);
    }

    @Override
    public Page<AuthorityDTO> searchProfilesByUserId(Long userId, String key, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        key = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";

        String baseQuery = """
    from v_user_profile vup 
    where vup.user_id = coalesce(?1, vup.user_id) 

    and (upper(unaccent(vup.profile_name)) like ?2 or upper(unaccent(vup.profile_code)) like ?2 or upper(unaccent(vup.profile_description)) like ?2)
    """;

        String selectQuery = """
    select vup.profile_code, vup.profile_name, vup.profile_description, 
    vup.profile_type_code, vup.profile_type_name 
    """ + baseQuery + " order by vup.profile_name";

        String countQuery = "select count(*) " + baseQuery;

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery, Long.class);

        Long count = (Long) countQueryExecutor.setParameter(1, userId).setParameter(2, key).getSingleResult();

        List<Object[]> results = selectQueryExecutor.setParameter(1, userId).setParameter(2, key)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // Utilisation du constructeur spécialisé avec authType "PRFL"
        List<AuthorityDTO> content = results.stream()
                .map(row -> new AuthorityDTO(
                        (String) row[0], // profile_code
                        (String) row[1], // profile_name
                        (String) row[2], // profile_description
                        (String) row[3], // profile_type_code
                        (String) row[4], // profile_type_name
                        "PRFL" // authType pour assigner profileCode
                ))
                .collect(Collectors.toList());

        return new Page<>(content, count, page, size);
    }

    @Override
    public Page<UserProfileAssoDTO> searchUserProfileAssignments(Long userId, Long strId, String profileCode, String key, PageRequest pageRequest)
    {
        int page = pageRequest.getPage();
        int size = pageRequest.getSize();
        key = "%" + StringUtils.stripAccentsToUpperCase(key) + "%";

        // Get chaineSigles only if strId is not null
        String chaineSigles = null;
        if (strId != null) {
            chaineSigles = vStrRepo.getChaineSigles(strId);
        }

        String baseQuery = """
    from v_user_profile vup 
    where vup.user_id = coalesce(:userId, vup.user_id) 
    and vup.profile_code = coalesce(:profileCode, vup.profile_code)
    """
    + (chaineSigles != null ? " and vup.profile_str_chaine_sigles LIKE :chaineSigles " : "")
    + """
    and (
        upper(unaccent(vup.profile_name)) like :key 
        or upper(unaccent(vup.profile_code)) like :key 
        or upper(unaccent(vup.email)) like :key
        or upper(unaccent(vup.first_name)) like :key
        or upper(unaccent(vup.last_name)) like :key
        or upper(unaccent(vup.profile_str_name)) like :key
    )
    """;

        String selectQuery = """
    select vup.ass_id, vup.user_id, vup.email, vup.first_name, vup.last_name, 
    vup.profile_code, vup.profile_name, vup.profile_description,
    vup.profile_str_id, vup.profile_str_name, vup.user_profile_ass_type_code, 
    vup.user_profile_ass_type_name, vup.libelle, vup.starting_date, vup.ending_date,
    vup.ass_status_code, vup.ass_status_name, vup.ordre
    """ + baseQuery + " order by vup.ordre, vup.profile_name";

        String countQuery = "select count(*) " + baseQuery;

        var selectQueryExecutor = getEntityManager().createNativeQuery(selectQuery);
        var countQueryExecutor = getEntityManager().createNativeQuery(countQuery, Long.class);

        // Set parameters for count query
        countQueryExecutor.setParameter("userId", userId);
        countQueryExecutor.setParameter("profileCode", profileCode);
        countQueryExecutor.setParameter("key", key);
        // Only set chaineSigles parameter if strId is not null
        if (chaineSigles != null) {
            countQueryExecutor.setParameter("chaineSigles", chaineSigles + "%");
        }
        Long count = (Long) countQueryExecutor.getSingleResult();

        // Set parameters for select query
        selectQueryExecutor.setParameter("userId", userId);
        selectQueryExecutor.setParameter("profileCode", profileCode);
        selectQueryExecutor.setParameter("key", key);
        // Only set chaineSigles parameter if strId is not null
        if (chaineSigles != null) {
            selectQueryExecutor.setParameter("chaineSigles", chaineSigles + "%");
        }
        List<Object[]> results = selectQueryExecutor
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // Map results to UserProfileAssoDTO objects
        List<UserProfileAssoDTO> content = results.stream()
                .map(row -> {
                    UserProfileAssoDTO dto = new UserProfileAssoDTO();
                    dto.setId((Long) row[0]);
                    dto.setUserId((Long) row[1]);
                    dto.setEmail((String) row[2]);
                    // Combine first and last name for display
                    dto.setFirstName((String) row[3]);
                    dto.setLastName((String) row[4]);
                    dto.setProfileCode((String) row[5]);
                    dto.setProfileName((String) row[6]);
                    // Skip profile description as it's not in UserProfileAssoDTO

                    dto.setStrId((Long) row[8]);
                    dto.setStrName((String) row[9]);
                    dto.setUserProfileAssTypeCode((String) row[10]);
                    dto.setUserProfileAssTypeName((String) row[11]);
                    dto.setLibelle((String) row[12]);
                    // Convert java.sql.Date to LocalDate directly without string parsing
                    dto.setStartingDate(row[13] != null ? ((java.sql.Date) row[13]).toLocalDate() : null);
                    dto.setEndingDate(row[14] != null ? ((java.sql.Date) row[14]).toLocalDate() : null);
                    dto.setAssStatusCode((String) row[15]);
                    dto.setAssStatusName((String) row[16]);
                    dto.setOrdre(((Integer) row[17]).longValue());
                    return dto;
                })
                .collect(Collectors.toList());

        return new Page<>(content, count, page, size);
    }

    @Override
    public List<UserProfileAssoDTO> findActiveAndCurrentProfilesByUserId(Long userId) {
        if (userId == null) return List.of();

        String query = """
        select vup.ass_id, vup.user_id, vup.email, vup.first_name, vup.last_name, 
        vup.profile_code, vup.profile_name, vup.profile_description,
        vup.profile_str_id, vup.profile_str_name, vup.user_profile_ass_type_code, 
        vup.user_profile_ass_type_name, vup.libelle, vup.starting_date, vup.ending_date,
        vup.ass_status_code, vup.ass_status_name, vup.ordre
        from v_user_profile vup 
        where vup.user_id = ?1
        and vup.ass_status_code in ('STA_ASS_ACT', 'STA_ASS_CUR')
        order by vup.ordre, vup.profile_name
        """;

        var queryExecutor = getEntityManager().createNativeQuery(query);
        queryExecutor.setParameter(1, userId);

        List<Object[]> results = queryExecutor.getResultList();

        // Map results to UserProfileAssoDTO objects
        return results.stream()
                .map(row -> {
                    UserProfileAssoDTO dto = new UserProfileAssoDTO();
                    dto.setId((Long) row[0]);
                    dto.setUserId((Long) row[1]);
                    dto.setEmail((String) row[2]);
                    dto.setFirstName((String) row[3]);
                    dto.setLastName((String) row[4]);
                    dto.setProfileCode((String) row[5]);
                    dto.setProfileName((String) row[6]);
                    dto.setStrId((Long) row[8]);
                    dto.setStrName((String) row[9]);
                    dto.setUserProfileAssTypeCode((String) row[10]);
                    dto.setUserProfileAssTypeName((String) row[11]);
                    dto.setLibelle((String) row[12]);
                    dto.setStartingDate(row[13] != null ? ((java.sql.Date) row[13]).toLocalDate() : null);
                    dto.setEndingDate(row[14] != null ? ((java.sql.Date) row[14]).toLocalDate() : null);
                    dto.setAssStatusCode((String) row[15]);
                    dto.setAssStatusName((String) row[16]);
                    dto.setOrdre(((Integer) row[17]).longValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
