
# Xenforo-Kotlin!

Olá, esta é uma API do Xenforo para o Java/Kotlin!
Ele ao invés de usar a conexão API própria do XenForo, ele se conecta ao MySQL do site para fazer a integração de uma forma mais estável!

# Maven
Colocar como dependência
```xml
<repositories>  
   <repository>  
     <id>jitpack.io</id>  
     <url>https://jitpack.io</url>  
   </repository>  
</repositories>
<dependency>
   <groupId>com.github.uJ0hn</groupId>
   <artifactId>Xenforo-Kotlin</artifactId>
   <version>RELEASE</version>
</dependency>
```

# Configuração
Java:
```java
XFApi api = new XFBuilder()  
.setHost("localhost")  
.setPort("3306")  
.setDbName("xenforo")  
.setUser("root")  
.setPassword("password").build();
```
Kotlin:
```java
val api = XFBuilder()  
.setHost("localhost")  
.setPort("3306")  
.setDbName("xenforo")  
.setUser("root")  
.setPassword("password")
.build()!!
```

## Puxar Usuários
Java:
```java
User user = api.getUserByName("Player");
User user = api.getUserById(1);
```
Kotlin:
```java
val user = api.getUserByName("Player")
val user = api.getUserById(1)
```

## Puxar Grupos
Java:
```java
Group group = api.getGroupByName("Player");
Group group = api.getGroupById(1);
```
Kotlin:
```java
val group = api.getGroupByName("Player")
val group = api.getGroupById(1)
```
## Setar Grupos Primário e Secundários
Java:
```java
User user = api.getUserByID(1);  
Group primarygroup = api.getGroupById(1);  
user.setFirstGroup(primarygroup);  
Group[] secondsgroups = new Group[]{api.getGroupById(2), api.getGroupById(3)};  
/* A boolean é caso você queira que subescreva sobre os grupos secundarios existentes */  
user.setSecondGroup(secondsgroups, true);
```
Kotlin:
```kotlin
val user = api.getUserByID(1)!!  
val primarygroup = api.getGroupById(1)!!  
user.setFirstGroup(primarygroup)  
val secondsgroups = arrayOf(api.getGroupById(2)!!, api.getGroupById(2)!!)  
/* A boolean é caso você queira que subescreva sobre os grupos secundarios existentes */  
user.setSecondGroup(*secondsgroups, rmothers = true)
```
