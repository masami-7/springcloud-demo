package com.yl.gateway.controller;

import com.yl.gateway.service.DynamicRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    DynamicRouteService dynamicRouteService;

    @PostMapping("/add")
    public void add() throws URISyntaxException {
        List<FilterDefinition> filters = new ArrayList<>();
        FilterDefinition filter = new FilterDefinition("StripPrefix=1");
        filters.add(filter);
        List<PredicateDefinition> predicates = new ArrayList<>();
        PredicateDefinition predicateDefinition = new PredicateDefinition("Path=/user/**");
        predicates.add(predicateDefinition);
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId("auth1");
        routeDefinition.setOrder(1);
        routeDefinition.setUri(new URI("lb://sso-auth"));
        routeDefinition.setFilters(filters);
        routeDefinition.setPredicates(predicates);
        dynamicRouteService.addRoute(routeDefinition);
    }

    @PostMapping("/delete")
    public void delete() throws URISyntaxException {
        dynamicRouteService.delete("auth1");
    }

}
