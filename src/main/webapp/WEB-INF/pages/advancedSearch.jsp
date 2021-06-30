<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Advanced search">
  <h1>Advanced search</h1>
  <form>
    <table>
      <tr>
        <td>
          Description
        </td>
        <td>
          <input name="description">
        </td>
        <td>
          <select name="descriptionOptions">
            <option>
              all words
            </option>
            <option>
              any word
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <td>
          Min price
        </td>
        <td>
          <input name="minPrice" type="number" min="0">
        </td>
      </tr>
      <tr>
        <td>
          Max price
        </td>
        <td>
          <input name="maxPrice" type="number" min="0">
        </td>
      </tr>
    </table>
    <button>Search</button>
  </form>
</tags:master>