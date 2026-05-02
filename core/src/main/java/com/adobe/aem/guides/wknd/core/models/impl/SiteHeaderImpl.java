/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.NavItem;
import com.adobe.aem.guides.wknd.core.models.SiteHeader;

@Model(
    adaptables = Resource.class,
    adapters = SiteHeader.class,
    resourceType = SiteHeaderImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SiteHeaderImpl implements SiteHeader {

    protected static final String RESOURCE_TYPE = "wknd/components/site-header";

    @ValueMapValue
    private String logoImage;

    @ValueMapValue
    private String logoLink;

    @ValueMapValue
    private String logoAlt;

    @ChildResource(name = "navItems")
    private List<Resource> navItemResources;

    private List<NavItem> navItems;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String ctaLink;

    @ValueMapValue
    private String languageLabel;

    @ValueMapValue(name = "showNotifications")
    private String showNotificationsStr;

    @ValueMapValue
    private String notificationsLink;

    @PostConstruct
    protected void init() {
        navItems = new ArrayList<>();
        if (navItemResources != null) {
            for (Resource itemResource : navItemResources) {
                NavItem navItem = itemResource.adaptTo(NavItem.class);
                if (navItem != null && navItem.hasContent()) {
                    navItems.add(navItem);
                }
            }
        }
    }

    @Override
    public String getLogoImage() {
        return logoImage;
    }

    @Override
    public String getLogoLink() {
        return logoLink;
    }

    @Override
    public String getLogoAlt() {
        return logoAlt;
    }

    @Override
    public List<NavItem> getNavItems() {
        return Collections.unmodifiableList(navItems);
    }

    @Override
    public String getCtaLabel() {
        return ctaLabel;
    }

    @Override
    public String getCtaLink() {
        return ctaLink;
    }

    @Override
    public String getLanguageLabel() {
        return languageLabel;
    }

    @Override
    public boolean isShowNotifications() {
        return Boolean.parseBoolean(showNotificationsStr);
    }

    @Override
    public String getNotificationsLink() {
        return notificationsLink;
    }

    @Override
    public boolean hasContent() {
        return logoImage != null || (navItems != null && !navItems.isEmpty()) || ctaLabel != null;
    }
}
