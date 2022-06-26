import * as cdk from 'aws-cdk-lib';
import {aws_route53_targets, Duration, Stack, StackProps} from 'aws-cdk-lib';
import {Construct} from 'constructs';
import * as path from 'path';
import * as ecs from 'aws-cdk-lib/aws-ecs';
import * as route53 from 'aws-cdk-lib/aws-route53';
import * as certificateManager from 'aws-cdk-lib/aws-certificatemanager';
import * as route53Patterns from 'aws-cdk-lib/aws-route53-patterns';
import {ContainerImage} from 'aws-cdk-lib/aws-ecs';
import * as ec2 from 'aws-cdk-lib/aws-ec2';
import * as ecsPatterns from 'aws-cdk-lib/aws-ecs-patterns';
import * as dynamodb from 'aws-cdk-lib/aws-dynamodb';
import {ApplicationProtocol, Protocol} from "aws-cdk-lib/aws-elasticloadbalancingv2";

// import * as sqs from 'aws-cdk-lib/aws-sqs';

export class CdkStack extends Stack {
  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    // 0. Get the hosted zone
    // Reference the Hosted Zone via its domain name
    const hostedZone = route53.HostedZone.fromLookup(this, 'articles-hosted-zone', {
      domainName: 'articles.app'
    });

    // Create a certificate for the domain. Ownership of the domain will be validated via DNS records created for us in the Hosted Zone.
    const certificate = new certificateManager.Certificate(this, 'articles-app-certificate-manager', {
      domainName: 'api.articles.app',
      validation: certificateManager.CertificateValidation.fromDns(hostedZone)
    });

    // 1. Create a VPC
    const vpc = new ec2.Vpc(this, 'Vpc', { maxAzs: 2 });

    // 2. Create a cluster
    const cluster = new ecs.Cluster(this, 'articles-cluster', { vpc: vpc });

    // 3. Create a Docker Image for the Service
    const image = ContainerImage.fromAsset(path.join(__dirname, '../../')); // todo: Will this work?
    // alt: const image = ecs.ContainerImage.fromRegistry("amazon/amazon-ecs-sample") // todo: Update to my registry

    // 4. Create Fargate Service
    const service = new ecsPatterns.ApplicationLoadBalancedFargateService(this, 'articles-service', {
      assignPublicIp: false,
      cluster: cluster,
      cpu: 256,
      desiredCount: 1,
      memoryLimitMiB: 512,
      publicLoadBalancer: true,
      protocol: ApplicationProtocol.HTTPS,
      domainName: 'api.articles.app',
      domainZone: hostedZone,
      taskImageOptions: {
        image: image,
        containerPort: 8080
      },
      certificate
    });

    service.targetGroup.configureHealthCheck({
      port: '8080',
      path: '/actuator/health',
      protocol: Protocol.HTTP,
      interval: cdk.Duration.seconds(30),
      timeout: cdk.Duration.seconds(6),
      healthyThresholdCount: 2,
      unhealthyThresholdCount: 2,
      healthyHttpCodes: '200'
    });

    // 5. Set up AutoScaling policy
    const scaling = service.service.autoScaleTaskCount({ maxCapacity: 2 });
    scaling.scaleOnCpuUtilization('CpuScaling', {
      targetUtilizationPercent: 50,
      scaleInCooldown: cdk.Duration.seconds(60),
      scaleOutCooldown: cdk.Duration.seconds(60)
    });

    // todo: CloudMap for service discovery?
  }
}
