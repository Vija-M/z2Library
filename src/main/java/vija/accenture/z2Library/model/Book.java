package vija.accenture.z2Library.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.stereotype.Component;
import vija.accenture.z2Library.swagger.DescriptionVariables;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;


@ApiModel(description = "Model of the product data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Book {

    @ApiModelProperty(notes = "The unique id of the book")
    private Long id;

    @ApiModelProperty(notes = "Title of the book")
    @NotEmpty
    private String title;

    @ApiModelProperty(notes = "Author of the book")
    @NotEmpty
    private String author;

    @ApiModelProperty(notes = "Genre of the book")
    @NotEmpty
    private Genre genre;
    //private String genre;

    @ApiModelProperty(notes = "Quantity of pages")
    @Min(value = 1, message = DescriptionVariables.PAGES_MIN)
    private int pages;

    @ApiModelProperty(notes = "Type of cover of the book")
    @NotEmpty
   private Cover cover;
    //private String cover;

    @ApiModelProperty(notes = "Location of the book")
    @NotEmpty
    private String shelf;

}
