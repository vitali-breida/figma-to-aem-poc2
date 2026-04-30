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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.FooterBar;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {FooterBar.class},
        resourceType = {FooterBarImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterBarImpl implements FooterBar {

    protected static final String RESOURCE_TYPE = "wknd/components/footer-bar";

    @ValueMapValue
    private String title;

    @ChildResource(name = "navLinks")
    private Resource navLinksResource;

    @ChildResource(name = "socialIcons")
    private Resource socialIconsResource;

    @ChildResource(name = "legalLinks")
    private Resource legalLinksResource;

    private List<NavLink> navLinks;
    private List<SocialIcon> socialIcons;
    private List<NavLink> legalLinks;

    @PostConstruct
    private void init() {
        navLinks = buildNavLinks(navLinksResource);
        socialIcons = buildSocialIcons(socialIconsResource);
        legalLinks = buildNavLinks(legalLinksResource);
    }

    private List<NavLink> buildNavLinks(Resource parent) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<NavLink> result = new ArrayList<>();
        for (Resource child : parent.getChildren()) {
            ValueMap vm = child.getValueMap();
            String linkTitle = vm.get("linkTitle", String.class);
            String linkUrl = vm.get("linkUrl", String.class);
            if (StringUtils.isNotBlank(linkTitle) || StringUtils.isNotBlank(linkUrl)) {
                result.add(new NavLinkImpl(linkTitle, linkUrl));
            }
        }
        return Collections.unmodifiableList(result);
    }

    private List<SocialIcon> buildSocialIcons(Resource parent) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<SocialIcon> result = new ArrayList<>();
        for (Resource child : parent.getChildren()) {
            ValueMap vm = child.getValueMap();
            String iconPath = vm.get("iconPath", String.class);
            String linkUrl = vm.get("linkUrl", String.class);
            if (StringUtils.isNotBlank(iconPath) || StringUtils.isNotBlank(linkUrl)) {
                result.add(new SocialIconImpl(iconPath, linkUrl));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<NavLink> getNavLinks() {
        return navLinks;
    }

    @Override
    public List<SocialIcon> getSocialIcons() {
        return socialIcons;
    }

    @Override
    public List<NavLink> getLegalLinks() {
        return legalLinks;
    }

    @Override
    public boolean isEmpty() {
        return navLinks.isEmpty();
    }

    static class NavLinkImpl implements NavLink {
        private final String linkTitle;
        private final String linkUrl;

        NavLinkImpl(String linkTitle, String linkUrl) {
            this.linkTitle = linkTitle;
            this.linkUrl = linkUrl;
        }

        @Override
        public String getLinkTitle() {
            return linkTitle;
        }

        @Override
        public String getLinkUrl() {
            return linkUrl;
        }
    }

    static class SocialIconImpl implements SocialIcon {
        private final String iconPath;
        private final String linkUrl;

        SocialIconImpl(String iconPath, String linkUrl) {
            this.iconPath = iconPath;
            this.linkUrl = linkUrl;
        }

        @Override
        public String getIconPath() {
            return iconPath;
        }

        @Override
        public String getLinkUrl() {
            return linkUrl;
        }
    }
}
