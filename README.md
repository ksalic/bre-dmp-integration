# BloomReach DMP Integration

Integrating the BloomReach Experience Relevance module with a DMP will give marketeers the opportunity to use these large sets of structured and unstructured data to give the website visitor of a BloomReach Experience driven website a relevant web experience. 

 
## 1. Building and installing the module

First, build and install the module itself into your local Maven repository:
```bash
    $ mvn clean install
```


## 2a. Running the demo

Make sure you have build and installed the module locally, see the previous step. The demo project is located in the 
`./demo` folder. Build and install using the regular commands:
```bash
    $ cd demo
    $ mvn clean verify
    $ mvn -P cargo.run
```
In another terminal:
```bash
    $ cd demo/dmp
    $ mvn spring-boot:run
```

After startup, access the CMS at `http://localhost:8080/cms` and the site at `http://localhost:8080/site`.

The DMP application will be running on `http://localhost:9090/home`


## 2b. Adding the module to existing project *TODO*

