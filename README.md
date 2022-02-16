[![ubuntu-20.04](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/ubuntu-20.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/ubuntu-20.yml)
[![ubuntu-18.04](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/ubuntu-18.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/ubuntu-18.yml)
[![macos-11](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/macos-11.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/macos-11.yml)
[![macos-10.15](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/macos-10.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/macos-10.yml)
[![windows-2022](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/windows-2022.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/windows-2022.yml)
[![windows-2019](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/windows-2019.yml/badge.svg)](https://github.com/S010MON/multi-agent-surveillance/actions/workflows/windows-2019.yml)

# Multi-Agent Surveillance
A model using multiple agents that sense using visual and audible signals to detect within an unknown space.

![UML](https://github.com/S010MON/multi-agent-surveillance/blob/master/sshots/UML.png)

## Project Requirements
The following requirements must be installed to run the software as a developer:
- Java-17
- Gradle-7.3.1

## Build Instructions [command line]

#### 1. Clone the repo and move to the top level directory
```bash
git clone https://github.com/S010MON/multi-agent-surveillance
cd multi-agent-surveillance
```
#### 2. Build and run using gradle-7.3.1
```bash
gradle build 
gradle run
```

## Build Instructions [Intellij IDEA]
Clone the repository using the `Open From VCS` option and entering the url https://github.com/S010MON/multi-agent-surveillance

#### 1. Lombok
For the Lombok annotations to be compiled into boilerplate code, you must turn on the annotation processor in Intellij.  
`File` -> `Settings` -> `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`

Select the "Enable Annotation Processing" checkbox and click `Apply`

![Screenshot from 2022-02-16 09-20-55](https://user-images.githubusercontent.com/10490509/154225590-330e9f17-ff84-4265-8683-c7169605abaf.png)


#### 2. Lombok Plugin Requirement
This may not be required with the newest version of IDEA, but to be thorough add the Lombok plugin as a requirement:
`File` -> `Settings` -> `Build, Execution, Deployment` -> `Required Plugins` -> `+`

Click the plus button and search for Lombok in the menu.

![Screenshot from 2022-02-16 09-20-35](https://user-images.githubusercontent.com/10490509/154225976-f05bbbcd-44cf-42b2-914c-91d225c91d9f.png)

