# Labforward Code Challenge for Backend Engineer Candidate

This is a simple Hello World API for recruiting purposes. You, as a candidate, should work on the challenge on your own account. Please clone the repo to your account and create a PR with your solution. 

# Code Changes Description

Put Request has been implemented to update an existing greeting: 
Id of an existing greeting must be passed as Path Variable.Id field is optional in the Request Body, however if ID is provided in the request Body it should be the same as the one passed as Path variable

Dao layer has been implemented to have a better architecture(no functional changes) 

Swagger has been implemented

Changes made to POST request: Id has been made optional. If the Id is provided in the request body,the same will be used to store the message else a randomly generated ID will be used

EntityCreatedResponse is returned from the post operation. Location is provided in the header.

Junits have been created for the PUT operation. 

Fixed a Junit which was failing earlier

# Scope for improvement

Data Persistance can be implemented with the use of Database
Logging can be implemented


 
