# Hi, I'm [Diego Kraenau](https://www.linkedin.com/in/diegokraenau/)! 👋

I´m Diego Kraenau, a software engenieer student. In this repository you will find the documentation about the Project 1 of Java Bootcamp Everis.

## Documentation

Firts of all, you will need to download the [Endpoints](https://drive.google.com/file/d/1syET_MUYuCLY41rrA7FvWOxcpka9BZMA/view?usp=sharing) and then you will need to import it on the postman.

In the next step, you will need to start the ConfigServer and then start the remaining microservices with the command "docker-compose up" in each project.

- [ConfigRepo](https://github.com/DiegoKraenauEveris/ConfigRepo)
- [ConfigServer](https://github.com/DiegoKraenauEveris/ConfigServer_P1)
- [EurekaServer](https://github.com/DiegoKraenauEveris/EurekaServer_P1)
- [Gateway](https://github.com/DiegoKraenauEveris/Gateway_P1.git)
- [CustomerMicroservice](https://github.com/DiegoKraenauEveris/Customer_Microservice_P1)
- [AccountMicroservice](https://github.com/DiegoKraenauEveris/Account_Microservice_P1)
- [TransactionMicroservice](https://github.com/DiegoKraenauEveris/Transaction_Microservice_P1)

## Microservices Reference

| Microservice | Port in local machine    | IP in docker container    | Port in docker container | IP in local machine |
| :-------- | :------- | :------- | :------- | :------- |
| MongoServer      | 28017 | 172.20.0.3| 27017| localhost|
| Gateway      | 8080 | 172.20.0.5| 8080| localhost|
| Eureka      | 8761 | 172.20.0.4| 8761| localhost|
| ConfigServer      | 8888 | 172.20.0.2| 8888| localhost|
| CustomerMicroservice      | 8081 | 172.20.0.6| 8081| localhost|
| AccountMicroservice      | 8082 | 172.20.0.7| 8082| localhost|
| TransactionMicroservice      | 8083 | 172.20.0.8| 8083| localhost|

After you start the microservices, you can use the endpoints that you imported on the postman, to recreate the process of the project you can see main endpoints 

![Endpoints](https://cdn.discordapp.com/attachments/635630482901434379/902986119791136819/unknown.png)

So, you need to run these endpoints sequentially to recreate the process.

Finally, you could see the diagrams of the microservices in this [link](https://drive.google.com/file/d/1TggPVqkG3WhqbxdBfyeCV-8V5EWQ7TTK/view?usp=sharing).
