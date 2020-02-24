package com.bisnode.opa.client.policy;

/**
 * This is the interface responsible for OPA Policy Api @see <a href=https://www.openpolicyagent.org/docs/latest/rest-api/#policy-api>OPA Policy API docs</a>
 */
public interface OpaPolicyApi {
    /**
     * <p>Updates or creates new OPA policy
     * </p>
     *
     * @param policy document to be created/updated
     * @since 0.0.1
     */
    void createOrUpdatePolicy(OpaPolicy policy);
}
