package hu.bearmaster.minecraftstarter.service;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.bearmaster.minecraftstarter.domain.Ec2Instance;
import software.amazon.awssdk.services.autoscaling.AutoScalingClient;
import software.amazon.awssdk.services.autoscaling.model.AutoScalingGroup;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsRequest;
import software.amazon.awssdk.services.autoscaling.model.DescribeAutoScalingGroupsResponse;
import software.amazon.awssdk.services.autoscaling.model.Instance;
import software.amazon.awssdk.services.autoscaling.model.UpdateAutoScalingGroupRequest;
import software.amazon.awssdk.services.ec2.EC2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;

@Service
public class AwsService {

    private final AutoScalingClient autoScalingClient;
    private final EC2Client ec2Client;

    public AwsService(AutoScalingClient autoScalingClient, EC2Client ec2Client) {
        this.autoScalingClient = autoScalingClient;
        this.ec2Client = ec2Client;
    }

    public List<String> getInstancesIdsOfAutoScalingGroup(String autoScalingGroupName) {
        DescribeAutoScalingGroupsRequest autoScalingGroupsRequest = DescribeAutoScalingGroupsRequest.builder()
                .autoScalingGroupNames(autoScalingGroupName)
                .build();

        DescribeAutoScalingGroupsResponse autoScalingGroupsResponse =
                autoScalingClient.describeAutoScalingGroups(autoScalingGroupsRequest);

        List<AutoScalingGroup> autoScalingGroups = autoScalingGroupsResponse.autoScalingGroups();

        return autoScalingGroups.size() > 0
                ? autoScalingGroups.get(0).instances().stream().map(Instance::instanceId).collect(toList())
                : Collections.emptyList();
    }

    public void upscaleAutoScalingGroup(String autoScalingGroupName) {
        setAutoScalingGroupCapacity(autoScalingGroupName, 1);
    }

    public void downscaleAutoScalingGroup(String autoScalingGroupName) {
        setAutoScalingGroupCapacity(autoScalingGroupName, 0);
    }

    public List<Ec2Instance> getInstancesByIds(List<String> instanceIds) {
        DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                .instanceIds(instanceIds)
                .build();
        DescribeInstancesResponse describeInstancesResponse = ec2Client.describeInstances(describeInstancesRequest);
        return describeInstancesResponse.reservations().stream()
                .flatMap(reservation -> reservation.instances().stream())
                .map(Ec2Instance::new)
                .collect(toList());
    }

    private void setAutoScalingGroupCapacity(String autoScalingGroupName, int capacity) {
        UpdateAutoScalingGroupRequest updateAutoScalingGroupRequest = UpdateAutoScalingGroupRequest.builder()
                .autoScalingGroupName(autoScalingGroupName)
                .desiredCapacity(capacity)
                .minSize(capacity)
                .maxSize(capacity)
                .build();
        autoScalingClient.updateAutoScalingGroup(updateAutoScalingGroupRequest);
    }
}
