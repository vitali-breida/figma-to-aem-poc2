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

import com.adobe.aem.guides.wknd.core.models.FindJobs;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {FindJobs.class},
        resourceType = {FindJobsImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FindJobsImpl implements FindJobs {

    protected static final String RESOURCE_TYPE = "wknd/components/find-jobs";

    @ValueMapValue
    private String searchPlaceholder;

    @ValueMapValue
    private String buttonLabel;

    @ValueMapValue
    private String countriesLabel;

    @ValueMapValue
    private String teamsLabel;

    @ValueMapValue
    private String expertisesLabel;

    @ChildResource(name = "countriesItems")
    private Resource countriesItemsResource;

    @ChildResource(name = "teamsItems")
    private Resource teamsItemsResource;

    @ChildResource(name = "expertisesItems")
    private Resource expertisesItemsResource;

    private List<DropdownOption> countriesItems;
    private List<DropdownOption> teamsItems;
    private List<DropdownOption> expertisesItems;

    @PostConstruct
    private void init() {
        countriesItems = buildOptions(countriesItemsResource);
        teamsItems = buildOptions(teamsItemsResource);
        expertisesItems = buildOptions(expertisesItemsResource);
    }

    private List<DropdownOption> buildOptions(Resource parent) {
        if (parent == null) {
            return Collections.emptyList();
        }
        List<DropdownOption> result = new ArrayList<>();
        for (Resource child : parent.getChildren()) {
            ValueMap vm = child.getValueMap();
            String text = vm.get("text", String.class);
            String value = vm.get("value", String.class);
            if (StringUtils.isNotBlank(text) || StringUtils.isNotBlank(value)) {
                result.add(new DropdownOptionImpl(text, value));
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public String getSearchPlaceholder() {
        return StringUtils.defaultIfBlank(searchPlaceholder, "Find job");
    }

    @Override
    public String getButtonLabel() {
        return StringUtils.defaultIfBlank(buttonLabel, "FIND JOBS");
    }

    @Override
    public String getCountriesLabel() {
        return StringUtils.defaultIfBlank(countriesLabel, "Countries");
    }

    @Override
    public List<DropdownOption> getCountriesItems() {
        return countriesItems;
    }

    @Override
    public String getTeamsLabel() {
        return StringUtils.defaultIfBlank(teamsLabel, "Teams");
    }

    @Override
    public List<DropdownOption> getTeamsItems() {
        return teamsItems;
    }

    @Override
    public String getExpertisesLabel() {
        return StringUtils.defaultIfBlank(expertisesLabel, "Expertises");
    }

    @Override
    public List<DropdownOption> getExpertisesItems() {
        return expertisesItems;
    }

    @Override
    public boolean hasContent() {
        return true;
    }

    static class DropdownOptionImpl implements DropdownOption {
        private final String text;
        private final String value;

        DropdownOptionImpl(String text, String value) {
            this.text = text;
            this.value = value;
        }

        @Override
        public String getText() {
            return text;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
}
