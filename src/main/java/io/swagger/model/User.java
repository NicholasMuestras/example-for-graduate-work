package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * User
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2026-01-26T20:45:26.875901102Z[GMT]")


public class User   {
  @JsonProperty("id")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Integer id = null;

  @JsonProperty("email")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String email = null;

  @JsonProperty("firstName")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String firstName = null;

  @JsonProperty("lastName")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String lastName = null;

  @JsonProperty("phone")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String phone = null;

  /**
   * роль пользователя
   */
  public enum RoleEnum {
    USER("USER"),
    
    ADMIN("ADMIN");

    private String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String text) {
      for (RoleEnum b : RoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("role")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private RoleEnum role = null;

  @JsonProperty("image")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String image = null;


  public User id(Integer id) { 

    this.id = id;
    return this;
  }

  /**
   * id пользователя
   * @return id
   **/
  
  @Schema(description = "id пользователя")
  
  public Integer getId() {  
    return id;
  }



  public void setId(Integer id) { 
    this.id = id;
  }

  public User email(String email) { 

    this.email = email;
    return this;
  }

  /**
   * логин пользователя
   * @return email
   **/
  
  @Schema(description = "логин пользователя")
  
  public String getEmail() {  
    return email;
  }



  public void setEmail(String email) { 
    this.email = email;
  }

  public User firstName(String firstName) { 

    this.firstName = firstName;
    return this;
  }

  /**
   * имя пользователя
   * @return firstName
   **/
  
  @Schema(description = "имя пользователя")
  
  public String getFirstName() {  
    return firstName;
  }



  public void setFirstName(String firstName) { 
    this.firstName = firstName;
  }

  public User lastName(String lastName) { 

    this.lastName = lastName;
    return this;
  }

  /**
   * фамилия пользователя
   * @return lastName
   **/
  
  @Schema(description = "фамилия пользователя")
  
  public String getLastName() {  
    return lastName;
  }



  public void setLastName(String lastName) { 
    this.lastName = lastName;
  }

  public User phone(String phone) { 

    this.phone = phone;
    return this;
  }

  /**
   * телефон пользователя
   * @return phone
   **/
  
  @Schema(description = "телефон пользователя")
  
  public String getPhone() {  
    return phone;
  }



  public void setPhone(String phone) { 
    this.phone = phone;
  }

  public User role(RoleEnum role) { 

    this.role = role;
    return this;
  }

  /**
   * роль пользователя
   * @return role
   **/
  
  @Schema(description = "роль пользователя")
  
  public RoleEnum getRole() {  
    return role;
  }



  public void setRole(RoleEnum role) { 
    this.role = role;
  }

  public User image(String image) { 

    this.image = image;
    return this;
  }

  /**
   * ссылка на аватар пользователя
   * @return image
   **/
  
  @Schema(description = "ссылка на аватар пользователя")
  
  public String getImage() {  
    return image;
  }



  public void setImage(String image) { 
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
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.phone, user.phone) &&
        Objects.equals(this.role, user.role) &&
        Objects.equals(this.image, user.image);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, firstName, lastName, phone, role, image);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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
