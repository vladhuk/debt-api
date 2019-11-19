# Debt API

## Table of contents
  1. [Description](#description)
  1. [API](#api)
  1. [Authentication](#authentication)

## Description
Service "Debt" is intended for creation debts between users. After registration and authentication you can add users to friendlist or to the blacklist. Users in blacklist can't send requests to you. If user confirms your request for frienship, you will be added to him friends list too. Now you can send debt requests to him. 

Debt request consists of comment, amount of money and receivers, which should to confirm or reject your request. Only when all users in request confirm it, debts will be updated. Even the one user rejects a request, it will be canceled, after that other members of request can't to confirm or reject it. If request is confirmed and there are no debts between 2 users, new debt will be created and it will be deleted only when balance will become 0.

You can also send repayment request, but only to one friend. Repayment request can be sent only if you have a debt balance with this user. As friend and debt request, repayment request can be confirmed or rejected.

Any request can be canceled by sender, if it is not confirmed or rejected.

Can be only one debt balance between 2 users, which updated after confirming of debt or repayment request. You can not delete frienship or add user to the blacklist if you have debt balance with him.

**[⬆ Back to top](#table-of-contents)**

## API
**[⬆ Back to top](#table-of-contents)**

## Authentication
**[⬆ Back to top](#table-of-contents)**
