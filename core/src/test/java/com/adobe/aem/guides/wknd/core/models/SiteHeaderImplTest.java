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
package com.adobe.aem.guides.wknd.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import com.adobe.aem.guides.wknd.core.models.impl.SiteHeaderImpl;

@ExtendWith(AemContextExtension.class)
class SiteHeaderImplTest {

    private final AemContext context = new AemContext();

    private Page page;
    private Resource componentResource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(SiteHeaderImpl.class, NavItem.class);
        page = context.create().page("/content/wknd/test-page");
    }

    @Test
    void testWithCompleteData() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header",
            "logoImage", "/content/dam/wknd/logo.png",
            "logoLink", "/content/wknd/home",
            "logoAlt", "Danone",
            "ctaLabel", "TRABAJOS",
            "ctaLink", "/content/wknd/jobs",
            "languageLabel", "EN",
            "showNotifications", "true",
            "notificationsLink", "/content/wknd/notifications");

        context.create().resource(componentResource, "navItems/item0",
            "label", "EQUIPOS",
            "link", "/content/wknd/equipos",
            "highlighted", "false",
            "hasDropdown", "false");
        context.create().resource(componentResource, "navItems/item1",
            "label", "MARCAS",
            "link", "/content/wknd/marcas",
            "highlighted", "true",
            "hasDropdown", "false");
        context.create().resource(componentResource, "navItems/item2",
            "label", "SOBRE NOSOTROS",
            "link", "/content/wknd/sobre-nosotros",
            "highlighted", "false",
            "hasDropdown", "true");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertEquals("/content/dam/wknd/logo.png", model.getLogoImage());
        assertEquals("/content/wknd/home", model.getLogoLink());
        assertEquals("Danone", model.getLogoAlt());
        assertEquals(3, model.getNavItems().size());
        assertEquals("MARCAS", model.getNavItems().get(1).getLabel());
        assertTrue(model.getNavItems().get(1).isHighlighted());
        assertTrue(model.getNavItems().get(2).isHasDropdown());
        assertEquals("TRABAJOS", model.getCtaLabel());
        assertEquals("/content/wknd/jobs", model.getCtaLink());
        assertEquals("EN", model.getLanguageLabel());
        assertTrue(model.isShowNotifications());
        assertEquals("/content/wknd/notifications", model.getNotificationsLink());
        assertTrue(model.hasContent());
    }

    @Test
    void testWhenEmpty() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertNull(model.getLogoImage());
        assertNull(model.getCtaLabel());
        assertNotNull(model.getNavItems());
        assertTrue(model.getNavItems().isEmpty());
        assertFalse(model.isShowNotifications());
        assertFalse(model.hasContent());
    }

    @Test
    void testWithLogoOnly() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header",
            "logoImage", "/content/dam/wknd/logo.png",
            "logoAlt", "WKND");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertEquals("/content/dam/wknd/logo.png", model.getLogoImage());
        assertNull(model.getLogoLink());
        assertTrue(model.hasContent());
    }

    @Test
    void testNavItemsWithNoItems() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header",
            "ctaLabel", "TRABAJOS");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertNotNull(model.getNavItems());
        assertTrue(model.getNavItems().isEmpty());
        assertTrue(model.hasContent());
    }

    @Test
    void testNavItemWithDropdown() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header",
            "logoImage", "/content/dam/wknd/logo.png");

        context.create().resource(componentResource, "navItems/item0",
            "label", "SOBRE NOSOTROS",
            "link", "/content/wknd/sobre",
            "highlighted", "false",
            "hasDropdown", "true");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertEquals(1, model.getNavItems().size());
        NavItem item = model.getNavItems().get(0);
        assertEquals("SOBRE NOSOTROS", item.getLabel());
        assertFalse(item.isHighlighted());
        assertTrue(item.isHasDropdown());
    }

    @Test
    void testShowNotificationsWhenFalse() {
        componentResource = context.create().resource(page, "site-header",
            "sling:resourceType", "wknd/components/site-header",
            "logoImage", "/content/dam/wknd/logo.png",
            "showNotifications", "false");

        SiteHeader model = componentResource.adaptTo(SiteHeader.class);

        assertNotNull(model);
        assertFalse(model.isShowNotifications());
    }
}
