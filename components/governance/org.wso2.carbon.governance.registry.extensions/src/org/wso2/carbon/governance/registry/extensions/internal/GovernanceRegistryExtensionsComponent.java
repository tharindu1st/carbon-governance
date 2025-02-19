/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.governance.registry.extensions.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.governance.common.GovernanceConfigurationService;
import org.wso2.carbon.governance.registry.extensions.listeners.RxtLoader;
import org.wso2.carbon.governance.registry.extensions.utils.CommonUtil;
import org.wso2.carbon.registry.core.service.RegistryService;
import org.wso2.carbon.registry.extensions.services.RXTStoragePathService;
import org.wso2.carbon.securevault.SecretCallbackHandlerService;
import org.wso2.carbon.utils.Axis2ConfigurationContextObserver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(
         name = "org.wso2.governance.registry.extensions.services", 
         immediate = true)
public class GovernanceRegistryExtensionsComponent {

    private static final Log log = LogFactory.getLog(GovernanceRegistryExtensionsComponent.class);

    private static SecretCallbackHandlerService secretCallbackHandlerService = null;

    private GovernanceRegistryExtensionsDataHolder dataHolder = GovernanceRegistryExtensionsDataHolder.getInstance();

    @Activate
    protected void activate(ComponentContext componentContext) {
        BundleContext bundleCtx = componentContext.getBundleContext();
        RxtLoader rxtLoader = new RxtLoader();
        CommonUtil.loadDependencyGraphMaxDepthConfig();
        ServiceRegistration tenantMgtListenerSR = bundleCtx.registerService(Axis2ConfigurationContextObserver.class.getName(), rxtLoader, null);
        if (tenantMgtListenerSR != null) {
            log.debug("Identity Provider Management - RXTLoader registered");
        } else {
            log.error("Identity Provider Management - RXTLoader could not be registered");
        }
        if (log.isDebugEnabled()) {
            log.debug("GovernanceRegistryExtensionsComponent activated");
        }
    }

    @Reference(
             name = "registry.service", 
             service = org.wso2.carbon.registry.core.service.RegistryService.class, 
             cardinality = ReferenceCardinality.MANDATORY, 
             policy = ReferencePolicy.DYNAMIC, 
             unbind = "unsetRegistryService")
    protected void setRegistryService(RegistryService registryService) {
        dataHolder.setRegistryService(registryService);
        if (log.isDebugEnabled()) {
            log.debug("Registry service initialized");
        }
    }

    protected void unsetRegistryService(RegistryService registryService) {
        dataHolder.setRegistryService(null);
    }

    @Reference(
             name = "secret.callback.handler.service", 
             service = org.wso2.carbon.securevault.SecretCallbackHandlerService.class, 
             cardinality = ReferenceCardinality.MANDATORY, 
             policy = ReferencePolicy.DYNAMIC, 
             unbind = "unsetSecretCallbackHandlerService")
    protected void setSecretCallbackHandlerService(SecretCallbackHandlerService secretCallbackHandlerService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting SecretCallbackHandlerService");
        }
        this.secretCallbackHandlerService = secretCallbackHandlerService;
    }

    protected void unsetSecretCallbackHandlerService(SecretCallbackHandlerService secretCallbackHandlerService) {
        this.secretCallbackHandlerService = null;
    }

    public static SecretCallbackHandlerService getSecretCallbackHandlerService() {
        return secretCallbackHandlerService;
    }

    @Reference(
             name = "extensions.service", 
             service = org.wso2.carbon.registry.extensions.services.RXTStoragePathService.class, 
             cardinality = ReferenceCardinality.MANDATORY, 
             policy = ReferencePolicy.DYNAMIC, 
             unbind = "unsetRxtStoragePathService")
    protected void setRxtStoragePathService(RXTStoragePathService rxtStoragePathService) {
        CommonUtil.setRxtStoragePathService(rxtStoragePathService);
    }

    protected void unsetRxtStoragePathService(RXTStoragePathService rxtSPService) {
        CommonUtil.setRxtStoragePathService(null);
    }

    @Reference(
             name = "governanceconfiguration.service", 
             service = org.wso2.carbon.governance.common.GovernanceConfigurationService.class, 
             cardinality = ReferenceCardinality.MANDATORY, 
             policy = ReferencePolicy.DYNAMIC, 
             unbind = "unsetGovernanceConfiguration")
    protected void setGovernanceConfigurationService(GovernanceConfigurationService govConfigService) {
        dataHolder.setGovernanceConfiguration(govConfigService.getGovernanceConfiguration());
    }

    protected void unsetGovernanceConfiguration(GovernanceConfigurationService govConfigService) {
        dataHolder.setGovernanceConfiguration(null);
    }
}

