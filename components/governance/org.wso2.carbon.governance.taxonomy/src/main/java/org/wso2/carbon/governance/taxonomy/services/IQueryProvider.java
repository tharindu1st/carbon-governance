/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.governance.taxonomy.services;

import org.w3c.dom.NodeList;
import org.wso2.carbon.governance.taxonomy.beans.QueryBean;
import org.wso2.carbon.governance.taxonomy.beans.TaxonomyBean;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.user.api.UserStoreException;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;

/**
 * This interface will provide methods for making queries
 */
public interface IQueryProvider {

    NodeList query(QueryBean taxonomyQueryBean) throws XPathExpressionException;

    String getUpdatedQuery(QueryBean taxonomyQueryBean);

    List<String> getTaxonomiesByRXT(String artifactType);

    String getTaxonomyNameById (QueryBean taxonomyQueryBean)
            throws UserStoreException, RegistryException, XPathExpressionException;

}
