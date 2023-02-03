package com.github.mdeluise.pinboard.integration.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mdeluise.pinboard.authorization.permission.PermissionService;
import com.github.mdeluise.pinboard.list.PageListDTO;
import com.github.mdeluise.pinboard.list.PageListService;
import com.github.mdeluise.pinboard.page.PageDTO;
import com.github.mdeluise.pinboard.page.PageService;
import com.github.mdeluise.pinboard.page.body.PageBodyService;
import com.github.mdeluise.pinboard.tag.TagDTO;
import com.github.mdeluise.pinboard.tag.TagService;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class IntegrationSteps {
    private final PermissionService permissionService;
    private final PageService pageService;
    private final TagService tagService;
    private final PageListService pageListService;
    private final PageBodyService pageBodyService;
    private final MockMvc mockMvc;
    private final StepData stepData;
    private final ObjectMapper objectMapper;
    private final String pageEndpoint = "/page";
    private final String tagEndpoint = "/tag";
    private final String pageListEndpoint = "/list";


    public IntegrationSteps(PermissionService permissionService, PageService pageService, TagService tagService,
                            PageListService pageListService, PageBodyService pageBodyService, MockMvc mockMvc,
                            StepData stepData, ObjectMapper objectMapper) {
        this.permissionService = permissionService;
        this.pageService = pageService;
        this.tagService = tagService;
        this.pageListService = pageListService;
        this.pageBodyService = pageBodyService;
        this.mockMvc = mockMvc;
        this.stepData = stepData;
        this.objectMapper = objectMapper;
    }


    @When("call GET {string}")
    public void callGet(String url) throws Exception {
        stepData.setResultActions(mockMvc.perform(
            MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON)
                                  .cookie(stepData.getCookie().orElse(new Cookie("dummy", "foo")))));
    }


    @When("call POST {string} with body {string}")
    public void callPostWithBody(String url, String body) throws Exception {
        stepData.setResultActions(mockMvc.perform(
            MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON)
                                  .cookie(stepData.getCookie().orElse(new Cookie("dummy", "foo"))).content(body)));
    }


    @When("call PUT {string} with body {string}")
    public void callPutWithBody(String url, String body) throws Exception {
        stepData.setResultActions(mockMvc.perform(
            MockMvcRequestBuilders.get(url).contentType(MediaType.APPLICATION_JSON)
                                  .cookie(stepData.getCookie().orElse(new Cookie("dummy", "foo"))).content(body)));
    }


    @When("call DELETE {string} with body {string}")
    public void callDeleteWithBody(String url, String body) throws Exception {
        stepData.setResultActions(mockMvc.perform(
            MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON)
                                  .cookie(stepData.getCookie().orElse(new Cookie("dummy", "foo"))).content(body)));
    }


    @Then("receive status code of {int}")
    public void theClientReceivesStatusCode(int status) throws Exception {
        stepData.getResultActions().andExpect(MockMvcResultMatchers.status().is(status));
    }


    @Given("the following pages")
    public void createPages(List<PageDTO> pages) throws Exception {
        for (PageDTO page : pages) {
            callPostWithBody(pageEndpoint, objectMapper.writeValueAsString(page));
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @DataTableType
    public PageDTO pageEntry(Map<String, String> entry) {
        PageDTO page = new PageDTO();
        page.setId(Long.parseLong(entry.get("id")));
        page.setUrl(entry.get("url"));
        if (entry.get("tags") != null) {
            page.setTagsName(Set.of(entry.get("tags").split(",")));
        }

        if (entry.get("lists") != null) {
            page.setListsName(Set.of(entry.get("lists").split(",")));
        }

        if (entry.get("img url") != null) {
            page.setHeaderImgUrl(entry.get("img url"));
        }
        return page;
    }


    @Given("the following tags")
    public void createTags(List<TagDTO> tags) throws Exception {
        for (TagDTO tag : tags) {
            callPostWithBody(tagEndpoint, objectMapper.writeValueAsString(tag));
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @DataTableType
    public TagDTO tagEntry(Map<String, String> entry) {
        TagDTO tag = new TagDTO();
        tag.setId(Long.parseLong(entry.get("id")));
        tag.setName(entry.get("name"));
        return tag;
    }


    @Given("the following lists")
    public void createPageLists(List<PageListDTO> pageLists) throws Exception {
        for (PageListDTO pageList : pageLists) {
            callPostWithBody(pageListEndpoint, objectMapper.writeValueAsString(pageList));
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @DataTableType
    public PageListDTO pageListEntry(Map<String, String> entry) {
        PageListDTO pageList = new PageListDTO();
        pageList.setId(Long.parseLong(entry.get("id")));
        pageList.setName(entry.get("name"));
        return pageList;
    }


    @Then("the count of pages in the list(s) {listOfLongs} is {int}")
    public void countThePagesInLists(List<Long> listIds, int expectedCount) throws Exception {
        String joinedIds = listIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callGet(pageListEndpoint + "/" + joinedIds + "/pages");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedCount)));
    }


    @Then("the count of pages tagged with {listOfLongs} is {int}")
    public void countThePagesTagged(List<Long> tagIds, int expectedCount) throws Exception {
        String joinedIds = tagIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callGet(tagEndpoint + "/" + joinedIds + "/pages");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedCount)));
    }


    @ParameterType("(?:\\d+,\\s*)*\\d+")
    public List<Long> listOfLongs(String arg) {
        return Arrays.stream(arg.split(",\\s?")).sequential().map(Long::parseLong).collect(Collectors.toList());
    }


    @And("cleanup the environment")
    @Transactional
    public void cleanupTheEnvironment() {
        pageService.getAll().forEach(page -> pageService.remove(page.getId()));
        tagService.getAll().forEach(tag -> tagService.remove(tag.getId()));
        pageListService.getAll().forEach(pageList -> pageListService.remove(pageList.getId()));
        pageBodyService.getAll().forEach(pageBody -> pageBodyService.remove(pageBody.getId()));
        permissionService.getAll().forEach(permissionService::remove);
    }


    @When("add the page(s) {listOfLongs} to the list(s) {listOfLongs}")
    public void addPagesToLists(List<Long> pageIds, List<Long> listIds) throws Exception {
        String joinedPageIds = pageIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String joinedListIds = listIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callPostWithBody(String.format("%s/%s/add-page/%s", pageListEndpoint, joinedListIds, joinedPageIds), "{}");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
    }


    @When("add the tag(s) {listOfLongs} to the page(s) {listOfLongs}")
    public void addTagsToPages(List<Long> tagIds, List<Long> pageIds) throws Exception {
        String joinedTagIds = tagIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String joinedPageIds = pageIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callPostWithBody(String.format("%s/%s/add-to-page/%s", tagEndpoint, joinedTagIds, joinedPageIds), "{}");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
    }


    @When("remove the page(s) {listOfLongs} from the list(s) {listOfLongs}")
    public void removePagesFromLists(List<Long> pageIds, List<Long> listIds) throws Exception {
        String joinedPageIds = pageIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String joinedListIds = listIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callPostWithBody(String.format("%s/%s/remove-page/%s", pageListEndpoint, joinedListIds, joinedPageIds), "{}");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
    }


    @When("remove the tag(s) {listOfLongs} from the page(s) {listOfLongs}")
    public void removeTagsFromPages(List<Long> tagIds, List<Long> pageIds) throws Exception {
        String joinedPageIds = pageIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        String joinedTagIds = tagIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        callPostWithBody(String.format("%s/%s/remove-from-page/%s", tagEndpoint, joinedTagIds, joinedPageIds), "{}");
        stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Then("the count of all pages is {int}")
    public void countAllPages(int expectedCount) throws Exception {
        callGet(pageEndpoint);
        stepData.getResultActions().andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedCount)));
    }


    @When("remove list(s) {listOfLongs}")
    public void removeLists(List<Long> listIds) throws Exception {
        for (Long listId : listIds) {
            callDeleteWithBody(pageListEndpoint + "/" + listId, "{}");
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @When("remove tag(s) {listOfLongs}")
    public void removeTags(List<Long> tagIds) throws Exception {
        for (Long tagId : tagIds) {
            callDeleteWithBody(tagEndpoint + "/" + tagId, "{}");
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @Then("the count of all lists is {int}")
    public void countAllLists(int expectedCount) throws Exception {
        callGet(pageListEndpoint);
        stepData.getResultActions().andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedCount)));
    }


    @Then("the count of all tags is {int}")
    public void countAllTags(int expectedCount) throws Exception {
        callGet(tagEndpoint);
        stepData.getResultActions().andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedCount)));
    }


    @When("remove page(s) {listOfLongs}")
    public void removePages(List<Long> pageIds) throws Exception {
        for (Long pageId : pageIds) {
            callDeleteWithBody(pageEndpoint + "/" + pageId, "{}");
            stepData.getResultActions().andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}
