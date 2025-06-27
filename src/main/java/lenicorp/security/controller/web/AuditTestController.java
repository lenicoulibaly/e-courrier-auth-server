package lenicorp.security.controller.web;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lenicorp.security.model.entities.AppUser;
import lenicorp.structures.model.entities.Structure;

import java.util.List;
import java.util.Map;

@Path("/open/audit-test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditTestController {
    
    @Inject
    EntityManager entityManager;
    
    @POST
    @Path("/test-user-audit")
    @Transactional
    public Response testUserAudit() {
        try {
            // Créer un utilisateur
            AppUser user = new AppUser();
            user.setEmail("test-" + System.currentTimeMillis() + "@example.com");
            user.setFirstName("Jean");
            user.setLastName("Dupont");
            user.setActivated(true);
            
            entityManager.persist(user);
            entityManager.flush();
            
            // Modifier l'utilisateur
            user.setFirstName("Jean-Pierre");
            user.setActivated(false);
            entityManager.flush();
            
            // Vérifier l'audit
            List<Object[]> auditEntries = getAppUserAuditEntries(user.getUserId());
            
            return Response.ok(Map.of(
                "message", "Test d'audit terminé avec succès",
                "userId", user.getUserId(),
                "auditEntries", auditEntries.size()
            )).build();
            
        } catch (Exception e) {
            return Response.serverError()
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @POST
    @Path("/test-structure-audit")
    @Transactional
    public Response testStructureAudit() {
        try {
            // Créer une structure
            Structure structure = new Structure();
            structure.setStrName("Direction Test " + System.currentTimeMillis());
            structure.setStrSigle("DT");
            structure.setStrAddress("123 Rue Test");
            
            entityManager.persist(structure);
            entityManager.flush();
            
            // Modifier la structure
            structure.setStrName(structure.getStrName() + " - Modifiée");
            structure.setStrSigle("DTM");
            entityManager.flush();
            
            // Vérifier l'audit
            List<Object[]> auditEntries = getStructureAuditEntries(structure.getStrId());
            
            return Response.ok(Map.of(
                "message", "Test d'audit Structure terminé avec succès",
                "strId", structure.getStrId(),
                "auditEntries", auditEntries.size()
            )).build();
            
        } catch (Exception e) {
            return Response.serverError()
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/audit-history/user/{userId}")
    public Response getUserAuditHistory(@PathParam("userId") Long userId) {
        try {
            List<Object[]> entries = getAppUserAuditEntries(userId);
            return Response.ok(entries).build();
        } catch (Exception e) {
            return Response.serverError()
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    @GET
    @Path("/audit-history/structure/{strId}")
    public Response getStructureAuditHistory(@PathParam("strId") Long strId) {
        try {
            List<Object[]> entries = getStructureAuditEntries(strId);
            return Response.ok(entries).build();
        } catch (Exception e) {
            return Response.serverError()
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    @DELETE
    @Path("/test-user-delete/{userId}")
    @Transactional
    public Response testUserDelete(@PathParam("userId") Long userId) {
        try {
            // Récupérer l'utilisateur avant suppression
            AppUser user = entityManager.find(AppUser.class, userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Utilisateur non trouvé"))
                        .build();
            }

            String userEmail = user.getEmail();

            // Supprimer l'utilisateur (cela créera un audit DELETE)
            entityManager.remove(user);
            entityManager.flush();

            // Vérifier l'entrée d'audit de suppression
            List<Object[]> deleteAuditEntries = getDeleteAuditEntries("audit_app_user_history", "user_id", userId);

            return Response.ok(Map.of(
                    "message", "Suppression testée avec succès",
                    "deletedUser", userEmail,
                    "userId", userId,
                    "deleteAuditEntries", deleteAuditEntries.size(),
                    "auditDetails", formatAuditEntries(deleteAuditEntries)
            )).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/test-structure-delete/{strId}")
    @Transactional
    public Response testStructureDelete(@PathParam("strId") Long strId) {
        try {
            // Récupérer la structure avant suppression
            Structure structure = entityManager.find(Structure.class, strId);
            if (structure == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Structure non trouvée"))
                        .build();
            }

            String structureName = structure.getStrName();

            // Supprimer la structure (cela créera un audit DELETE)
            entityManager.remove(structure);
            entityManager.flush();

            // Vérifier l'entrée d'audit de suppression
            List<Object[]> deleteAuditEntries = getDeleteAuditEntries("audit_structure_history", "str_id", strId);

            return Response.ok(Map.of(
                    "message", "Suppression de structure testée avec succès",
                    "deletedStructure", structureName,
                    "strId", strId,
                    "deleteAuditEntries", deleteAuditEntries.size(),
                    "auditDetails", formatAuditEntries(deleteAuditEntries)
            )).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(Map.of("error", "Erreur lors de la suppression: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/test-complete-lifecycle")
    @Transactional
    public Response testCompleteLifecycle() {
        try {
            // 1. CRÉER un utilisateur
            AppUser user = new AppUser();
            user.setEmail("lifecycle-test-" + System.currentTimeMillis() + "@example.com");
            user.setFirstName("Test");
            user.setLastName("Lifecycle");
            user.setActivated(false);

            entityManager.persist(user);
            entityManager.flush();

            Long userId = user.getUserId();

            // 2. MODIFIER l'utilisateur
            user.setFirstName("Test-Modified");
            user.setActivated(true);
            entityManager.flush();

            // 3. SUPPRIMER l'utilisateur
            entityManager.remove(user);
            entityManager.flush();

            // 4. Vérifier l'historique complet
            List<Object[]> completeHistory = getAppUserAuditEntries(userId);

            return Response.ok(Map.of(
                    "message", "Cycle de vie complet testé",
                    "userId", userId,
                    "totalAuditEntries", completeHistory.size(),
                    "lifecycle", Map.of(
                            "created", hasRevisionType(completeHistory, (short) 0),
                            "updated", hasRevisionType(completeHistory, (short) 1),
                            "deleted", hasRevisionType(completeHistory, (short) 2)
                    ),
                    "auditHistory", formatAuditEntries(completeHistory)
            )).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(Map.of("error", "Erreur lors du test complet: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/audit-summary")
    public Response getAuditSummary() {
        try {
            // Compter les entrées par type de révision
            var userAuditQuery = entityManager.createNativeQuery(
                    "SELECT revtype, COUNT(*) FROM audit.audit_app_user_history GROUP BY revtype ORDER BY revtype"
            );

            var structureAuditQuery = entityManager.createNativeQuery(
                    "SELECT revtype, COUNT(*) FROM audit.audit_structure_history GROUP BY revtype ORDER BY revtype"
            );

            @SuppressWarnings("unchecked")
            List<Object[]> userStats = userAuditQuery.getResultList();

            @SuppressWarnings("unchecked")
            List<Object[]> structureStats = structureAuditQuery.getResultList();

            return Response.ok(Map.of(
                    "userAuditStats", formatAuditStats(userStats),
                    "structureAuditStats", formatAuditStats(structureStats),
                    "legend", Map.of(
                            "0", "INSERT",
                            "1", "UPDATE",
                            "2", "DELETE"
                    )
            )).build();

        } catch (Exception e) {
            return Response.serverError()
                    .entity(Map.of("error", "Erreur lors de la récupération du résumé: " + e.getMessage()))
                    .build();
        }
    }

    // Méthodes utilitaires privées
    @SuppressWarnings("unchecked")
    private List<Object[]> getDeleteAuditEntries(String tableName, String idColumn, Long id) {
        var query = entityManager.createNativeQuery(
                "SELECT rev, revtype, created_by, updated_by " +
                        "FROM audit." + tableName + " " +
                        "WHERE " + idColumn + " = :id AND revtype = 2 " + // 2 = DELETE
                        "ORDER BY rev DESC"
        );
        query.setParameter("id", id);
        return query.getResultList();
    }

    private boolean hasRevisionType(List<Object[]> auditEntries, Short revType) {
        return auditEntries.stream()
                .anyMatch(entry -> revType.equals((Short) entry[1]));
    }

    private Object formatAuditEntries(List<Object[]> entries) {
        return entries.stream()
                .map(entry -> Map.of(
                        "revision", entry[0],
                        "type", getRevisionType((Short) entry[1]),
                        "details", entry.length > 2 ? java.util.Arrays.toString(java.util.Arrays.copyOfRange(entry, 2, entry.length)) : "N/A"
                ))
                .toList();
    }

    private Object formatAuditStats(List<Object[]> stats) {
        return stats.stream()
                .map(stat -> Map.of(
                        "operationType", getRevisionType((Short) stat[0]),
                        "count", stat[1]
                ))
                .toList();
    }

    private String getRevisionType(Short revType) {
        return switch (revType) {
            case 0 -> "INSERT";
            case 1 -> "UPDATE";
            case 2 -> "DELETE";
            default -> "UNKNOWN";
        };
    }
    
    @SuppressWarnings("unchecked")
    private List<Object[]> getAppUserAuditEntries(Long userId) {
        var query = entityManager.createNativeQuery(
            "SELECT rev, revtype, email, first_name, activated, created_by, updated_by " +
            "FROM audit.audit_app_user_history " +
            "WHERE user_id = :userId ORDER BY rev"
        );
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    private List<Object[]> getStructureAuditEntries(Long strId) {
        var query = entityManager.createNativeQuery(
            "SELECT rev, revtype, str_name, str_sigle, created_by, updated_by " +
            "FROM audit.audit_structure_history " +
            "WHERE str_id = :strId ORDER BY rev"
        );
        query.setParameter("strId", strId);
        return query.getResultList();
    }
}