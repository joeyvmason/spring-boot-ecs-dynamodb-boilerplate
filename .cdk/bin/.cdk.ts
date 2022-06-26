#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { CdkStack } from '../lib/.cdk-stack';
import {DynamoStack} from "../lib/.dynamo-stack";

const app = new cdk.App();
new CdkStack(app, 'CdkStack', {
  env: { account: process.env.AWS_ACCOUNT_ID, region: process.env.AWS_REGION }
});

new DynamoStack(app, 'DynamoStack', {
  env: { account: process.env.AWS_ACCOUNT_ID, region: process.env.AWS_REGION }
});