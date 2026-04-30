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
package com.adobe.aem.guides.wknd.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.aem.guides.wknd.core.models.impl.FindJobsImpl;
import com.day.cq.wcm.api.Page;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class FindJobsModelTest {

    private final AemContext context = new AemContext();

    private Page page;
    private Resource componentResource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(FindJobsImpl.class);
        page = context.create().page("/content/wknd/test-page");
    }

    @Test
    void testWithCompleteData() {
        componentResource = context.create().resource(page, "find-jobs",
            "sling:resourceType", "wknd/components/find-jobs",
            "searchPlaceholder", "Search for a role",
            "buttonLabel", "Search Now",
            "countriesLabel", "Country",
            "teamsLabel", "Team",
            "expertisesLabel", "Expertise");

        context.create().resource(componentResource, "countriesItems/item0",
            "text", "France", "value", "france");
        context.create().resource(componentResource, "countriesItems/item1",
            "text", "Spain", "value", "spain");
        context.create().resource(componentResource, "teamsItems/item0",
            "text", "Marketing", "value", "marketing");
        context.create().resource(componentResource, "expertisesItems/item0",
            "text", "Digital", "value", "digital");

        context.currentResource(componentResource);
        FindJobs model = context.request().adaptTo(FindJobs.class);

        assertNotNull(model);
        assertEquals("Search for a role", model.getSearchPlaceholder());
        assertEquals("Search Now", model.getButtonLabel());
        assertEquals("Country", model.getCountriesLabel());
        assertEquals("Team", model.getTeamsLabel());
        assertEquals("Expertise", model.getExpertisesLabel());

        assertEquals(2, model.getCountriesItems().size());
        assertEquals("France", model.getCountriesItems().get(0).getText());
        assertEquals("france", model.getCountriesItems().get(0).getValue());
        assertEquals("Spain", model.getCountriesItems().get(1).getText());

        assertEquals(1, model.getTeamsItems().size());
        assertEquals("Marketing", model.getTeamsItems().get(0).getText());

        assertEquals(1, model.getExpertisesItems().size());
        assertEquals("Digital", model.getExpertisesItems().get(0).getText());

        assertTrue(model.hasContent());
    }

    @Test
    void testDefaultValues() {
        componentResource = context.create().resource(page, "find-jobs",
            "sling:resourceType", "wknd/components/find-jobs");

        context.currentResource(componentResource);
        FindJobs model = context.request().adaptTo(FindJobs.class);

        assertNotNull(model);
        assertEquals("Find job", model.getSearchPlaceholder());
        assertEquals("FIND JOBS", model.getButtonLabel());
        assertEquals("Countries", model.getCountriesLabel());
        assertEquals("Teams", model.getTeamsLabel());
        assertEquals("Expertises", model.getExpertisesLabel());
        assertTrue(model.getCountriesItems().isEmpty());
        assertTrue(model.getTeamsItems().isEmpty());
        assertTrue(model.getExpertisesItems().isEmpty());
        assertTrue(model.hasContent());
    }

    @Test
    void testWithNoDropdownOptions() {
        componentResource = context.create().resource(page, "find-jobs",
            "sling:resourceType", "wknd/components/find-jobs",
            "countriesLabel", "Countries");

        context.currentResource(componentResource);
        FindJobs model = context.request().adaptTo(FindJobs.class);

        assertNotNull(model);
        assertNotNull(model.getCountriesItems());
        assertTrue(model.getCountriesItems().isEmpty());
        assertNotNull(model.getTeamsItems());
        assertTrue(model.getTeamsItems().isEmpty());
    }

    @Test
    void testWithMultipleOptionsPerDropdown() {
        componentResource = context.create().resource(page, "find-jobs",
            "sling:resourceType", "wknd/components/find-jobs");

        context.create().resource(componentResource, "teamsItems/item0",
            "text", "Marketing", "value", "marketing");
        context.create().resource(componentResource, "teamsItems/item1",
            "text", "Finance", "value", "finance");
        context.create().resource(componentResource, "teamsItems/item2",
            "text", "Engineering", "value", "engineering");

        context.currentResource(componentResource);
        FindJobs model = context.request().adaptTo(FindJobs.class);

        assertNotNull(model);
        assertEquals(3, model.getTeamsItems().size());
        assertEquals("Finance", model.getTeamsItems().get(1).getText());
        assertEquals("finance", model.getTeamsItems().get(1).getValue());
    }
}
