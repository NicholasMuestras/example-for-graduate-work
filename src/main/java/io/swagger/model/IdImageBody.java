package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.Resource;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * IdImageBody
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2026-01-26T20:45:26.875901102Z[GMT]")


public class IdImageBody   {
  @JsonProperty("image")

  private Resource image = null;


  public IdImageBody image(Resource image) { 

    this.image = image;
    return this;
  }

  /**
   * Get image
   * @return image
   **/
  
  @Schema(required = true, description = "")
  
@Valid
  @NotNull
  public Resource getImage() {  
    return image;
  }



  public void setImage(Resource image) { 

    this.image = image;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdImageBody idImageBody = (IdImageBody) o;
    return Objects.equals(this.image, idImageBody.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(image);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdImageBody {\n");
    
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
