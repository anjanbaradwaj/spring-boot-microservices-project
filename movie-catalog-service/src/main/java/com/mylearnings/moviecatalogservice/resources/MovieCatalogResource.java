package com.mylearnings.moviecatalogservice.resources;

import com.mylearnings.moviecatalogservice.models.CatalogItem;
import com.mylearnings.moviecatalogservice.service.MovieCatalogResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {


    @Autowired
    private MovieCatalogResourceService movieCatalogResourceService;

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        return movieCatalogResourceService.getCatalog(userId);
    }

}
