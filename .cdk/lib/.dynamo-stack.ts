import {Stack, StackProps} from "aws-cdk-lib";
import {Construct} from "constructs";
import * as dynamodb from "aws-cdk-lib/aws-dynamodb";

export class DynamoStack extends Stack {
    constructor(scope: Construct, id: string, props?: StackProps) {
        super(scope, id, props);

        // 6. Create DynamoDB tables
        const table = new dynamodb.Table(this, 'ExampleTable', {
            partitionKey: { name: 'id', type: dynamodb.AttributeType.STRING },
        });
    }
}