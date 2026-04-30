/*
 *  Copyright 2019 Adobe Systems Incorporated
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

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.aem.guides.wknd.core.models.FooterBar;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class FooterBarImplTest {

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(FooterBarImpl.class);
        ctx.load().json(
            "/com/adobe/aem/guides/wknd/core/models/impl/FooterBarImplTest.json",
            "/content"
        );
    }

    @Test
    void testGetTitle() {
        ctx.currentResource("/content/footer-bar");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertEquals("Site Footer", footerBar.getTitle());
    }

    @Test
    void testGetNavLinks() {
        ctx.currentResource("/content/footer-bar");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);

        List<FooterBar.NavLink> links = footerBar.getNavLinks();
        assertEquals(2, links.size());
        assertEquals("Teams", links.get(0).getLinkTitle());
        assertEquals("/content/wknd/teams", links.get(0).getLinkUrl());
        assertEquals("Brands", links.get(1).getLinkTitle());
    }

    @Test
    void testGetSocialIcons() {
        ctx.currentResource("/content/footer-bar");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);

        List<FooterBar.SocialIcon> icons = footerBar.getSocialIcons();
        assertEquals(2, icons.size());
        assertEquals("/content/dam/wknd/icons/facebook.svg", icons.get(0).getIconPath());
        assertEquals("https://www.facebook.com/danone", icons.get(0).getLinkUrl());
    }

    @Test
    void testGetLegalLinks() {
        ctx.currentResource("/content/footer-bar");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);

        List<FooterBar.NavLink> links = footerBar.getLegalLinks();
        assertEquals(2, links.size());
        assertEquals("Cookies", links.get(0).getLinkTitle());
        assertEquals("Privacy Policy", links.get(1).getLinkTitle());
    }

    @Test
    void testIsEmpty_WhenBlank() {
        ctx.currentResource("/content/empty");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertTrue(footerBar.isEmpty());
    }

    @Test
    void testIsEmpty_TitleOnlyIsEmpty() {
        ctx.currentResource("/content/title-only");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertTrue(footerBar.isEmpty());
    }

    @Test
    void testIsNotEmpty_WithNavLinks() {
        ctx.currentResource("/content/footer-bar");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertFalse(footerBar.isEmpty());
    }

    @Test
    void testEmptyCollections_WhenNoChildren() {
        ctx.currentResource("/content/empty");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertTrue(footerBar.getNavLinks().isEmpty());
        assertTrue(footerBar.getSocialIcons().isEmpty());
        assertTrue(footerBar.getLegalLinks().isEmpty());
    }

    @Test
    void testNoSocialIcons_ReturnsEmpty() {
        ctx.currentResource("/content/no-social");
        FooterBar footerBar = ctx.request().adaptTo(FooterBar.class);
        assertNotNull(footerBar);
        assertTrue(footerBar.getSocialIcons().isEmpty());
        assertEquals(1, footerBar.getNavLinks().size());
    }
}
