# Projekt

Simple Blogging-App, in der man Blog-Beiträge entdecken und liken kann.
## Beschreibung
Die Blogging-App hat folgende Eigenschaften
- Endpunkte und Login sind über SwaggerUI zugänglich
- Registrierung und login findet über Keycloak statt
- Blog-Beiträge (Posts) können von Nutzern erstellt werden
- Posts können aktualisiert werden
- Man kann einzelne Posts anfragen, aber auch mehrere Posts seitenweise abfragen.
- Posts können ebenfalls wieder gelöscht werden
- Man kann Posts liken und auch wieder ent-liken
- Die Anfragen werden im Backend robust überprüft. Beispielsweise kann nur der Besitzer ein Post aktualisieren oder löschen.

### Registrieren und Login über Keycloak
<img src="./images/keycloak-login.png" style="width:60%; height:auto;">

### SwaggerUI Endpunkte
<img src="./images/endpoints.png" style="width:60%; height:auto;">

### Beispiel für das Erstellen eines Blog-Beitrags
<img src="./images/post-create.png" style="width:60%; height:auto;">

### Beispiel für das Anfragen von Blog-Beiträgen mit 'Post' im Titel
<img src="./images/get-posts.png" style="width:60%; height:auto;">
<img src="./images/get-result.png" style="width:60%; height:auto;">

### Beispiel für einfaches Error-Handling beim Löschen eines nicht existierenden Post
<img src="./images/delete-error.png" style="width:60%; height:auto;">


## Verwendete Technologien
- Spring 
    - Spring Boot
    - Spring Data JPA
    - Spring Security
    - Spring Web
- Maven




