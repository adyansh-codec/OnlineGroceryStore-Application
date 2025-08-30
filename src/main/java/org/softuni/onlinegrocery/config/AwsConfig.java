package org.softuni.onlinegrocery.config;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.services.simpleemail.waiters.AmazonSimpleEmailServiceWaiters;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    // Mock SNS implementation for development
    public static class MockAmazonSNS implements AmazonSNS {
        @Override
        public PublishResult publish(PublishRequest publishRequest) {
            System.out.println("MOCK SMS - Phone: " + publishRequest.getPhoneNumber());
            System.out.println("MOCK SMS - Message: " + publishRequest.getMessage());
            PublishResult result = new PublishResult();
            result.setMessageId("mock-sms-id-" + System.currentTimeMillis());
            return result;
        }

        // All other methods throw UnsupportedOperationException
        @Override
        public void setEndpoint(String endpoint) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public void setRegion(com.amazonaws.regions.Region region) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public AddPermissionResult addPermission(AddPermissionRequest addPermissionRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public AddPermissionResult addPermission(String topicArn, String label, java.util.List<String> aWSAccountIds, java.util.List<String> actionNames) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CheckIfPhoneNumberIsOptedOutResult checkIfPhoneNumberIsOptedOut(CheckIfPhoneNumberIsOptedOutRequest checkIfPhoneNumberIsOptedOutRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ConfirmSubscriptionResult confirmSubscription(ConfirmSubscriptionRequest confirmSubscriptionRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ConfirmSubscriptionResult confirmSubscription(String topicArn, String token, String authenticateOnUnsubscribe) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ConfirmSubscriptionResult confirmSubscription(String topicArn, String token) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreatePlatformApplicationResult createPlatformApplication(CreatePlatformApplicationRequest createPlatformApplicationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreatePlatformEndpointResult createPlatformEndpoint(CreatePlatformEndpointRequest createPlatformEndpointRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateSMSSandboxPhoneNumberResult createSMSSandboxPhoneNumber(CreateSMSSandboxPhoneNumberRequest createSMSSandboxPhoneNumberRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateTopicResult createTopic(CreateTopicRequest createTopicRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateTopicResult createTopic(String name) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteEndpointResult deleteEndpoint(DeleteEndpointRequest deleteEndpointRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeletePlatformApplicationResult deletePlatformApplication(DeletePlatformApplicationRequest deletePlatformApplicationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteSMSSandboxPhoneNumberResult deleteSMSSandboxPhoneNumber(DeleteSMSSandboxPhoneNumberRequest deleteSMSSandboxPhoneNumberRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteTopicResult deleteTopic(DeleteTopicRequest deleteTopicRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteTopicResult deleteTopic(String topicArn) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetEndpointAttributesResult getEndpointAttributes(GetEndpointAttributesRequest getEndpointAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetPlatformApplicationAttributesResult getPlatformApplicationAttributes(GetPlatformApplicationAttributesRequest getPlatformApplicationAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSMSAttributesResult getSMSAttributes(GetSMSAttributesRequest getSMSAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSMSSandboxAccountStatusResult getSMSSandboxAccountStatus(GetSMSSandboxAccountStatusRequest getSMSSandboxAccountStatusRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSubscriptionAttributesResult getSubscriptionAttributes(GetSubscriptionAttributesRequest getSubscriptionAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSubscriptionAttributesResult getSubscriptionAttributes(String subscriptionArn) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetTopicAttributesResult getTopicAttributes(GetTopicAttributesRequest getTopicAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetTopicAttributesResult getTopicAttributes(String topicArn) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListEndpointsByPlatformApplicationResult listEndpointsByPlatformApplication(ListEndpointsByPlatformApplicationRequest listEndpointsByPlatformApplicationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListOriginationNumbersResult listOriginationNumbers(ListOriginationNumbersRequest listOriginationNumbersRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListPhoneNumbersOptedOutResult listPhoneNumbersOptedOut(ListPhoneNumbersOptedOutRequest listPhoneNumbersOptedOutRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListPlatformApplicationsResult listPlatformApplications(ListPlatformApplicationsRequest listPlatformApplicationsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListPlatformApplicationsResult listPlatformApplications() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSMSSandboxPhoneNumbersResult listSMSSandboxPhoneNumbers(ListSMSSandboxPhoneNumbersRequest listSMSSandboxPhoneNumbersRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsResult listSubscriptions(ListSubscriptionsRequest listSubscriptionsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsResult listSubscriptions() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsResult listSubscriptions(String nextToken) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsByTopicResult listSubscriptionsByTopic(ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String topicArn) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListSubscriptionsByTopicResult listSubscriptionsByTopic(String topicArn, String nextToken) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListTagsForResourceResult listTagsForResource(ListTagsForResourceRequest listTagsForResourceRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListTopicsResult listTopics(ListTopicsRequest listTopicsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListTopicsResult listTopics() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListTopicsResult listTopics(String nextToken) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public OptInPhoneNumberResult optInPhoneNumber(OptInPhoneNumberRequest optInPhoneNumberRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public PublishResult publish(String topicArn, String message) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public PublishResult publish(String topicArn, String message, String subject) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public PublishBatchResult publishBatch(PublishBatchRequest publishBatchRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public RemovePermissionResult removePermission(RemovePermissionRequest removePermissionRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public RemovePermissionResult removePermission(String topicArn, String label) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetEndpointAttributesResult setEndpointAttributes(SetEndpointAttributesRequest setEndpointAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetPlatformApplicationAttributesResult setPlatformApplicationAttributes(SetPlatformApplicationAttributesRequest setPlatformApplicationAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetSMSAttributesResult setSMSAttributes(SetSMSAttributesRequest setSMSAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetSubscriptionAttributesResult setSubscriptionAttributes(SetSubscriptionAttributesRequest setSubscriptionAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetSubscriptionAttributesResult setSubscriptionAttributes(String subscriptionArn, String attributeName, String attributeValue) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetTopicAttributesResult setTopicAttributes(SetTopicAttributesRequest setTopicAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetTopicAttributesResult setTopicAttributes(String topicArn, String attributeName, String attributeValue) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SubscribeResult subscribe(SubscribeRequest subscribeRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SubscribeResult subscribe(String topicArn, String protocol, String endpoint) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public TagResourceResult tagResource(TagResourceRequest tagResourceRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UnsubscribeResult unsubscribe(UnsubscribeRequest unsubscribeRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UnsubscribeResult unsubscribe(String subscriptionArn) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UntagResourceResult untagResource(UntagResourceRequest untagResourceRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public VerifySMSSandboxPhoneNumberResult verifySMSSandboxPhoneNumber(VerifySMSSandboxPhoneNumberRequest verifySMSSandboxPhoneNumberRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public void shutdown() {
            // No-op for mock
        }

        @Override
        public com.amazonaws.ResponseMetadata getCachedResponseMetadata(com.amazonaws.AmazonWebServiceRequest request) {
            return null;
        }
    }

    // Mock SES implementation for development
    public static class MockAmazonSES implements AmazonSimpleEmailService {
        @Override
        public SendEmailResult sendEmail(SendEmailRequest sendEmailRequest) {
            System.out.println("MOCK EMAIL - To: " + sendEmailRequest.getDestination().getToAddresses());
            System.out.println("MOCK EMAIL - Subject: " + sendEmailRequest.getMessage().getSubject().getData());
            System.out.println("MOCK EMAIL - Body: " + sendEmailRequest.getMessage().getBody().getText().getData());
            SendEmailResult result = new SendEmailResult();
            result.setMessageId("mock-email-id-" + System.currentTimeMillis());
            return result;
        }

        // All other methods throw UnsupportedOperationException
        @Override
        public void setEndpoint(String endpoint) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public void setRegion(com.amazonaws.regions.Region region) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CloneReceiptRuleSetResult cloneReceiptRuleSet(CloneReceiptRuleSetRequest cloneReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateConfigurationSetResult createConfigurationSet(CreateConfigurationSetRequest createConfigurationSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateConfigurationSetEventDestinationResult createConfigurationSetEventDestination(CreateConfigurationSetEventDestinationRequest createConfigurationSetEventDestinationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateConfigurationSetTrackingOptionsResult createConfigurationSetTrackingOptions(CreateConfigurationSetTrackingOptionsRequest createConfigurationSetTrackingOptionsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateCustomVerificationEmailTemplateResult createCustomVerificationEmailTemplate(CreateCustomVerificationEmailTemplateRequest createCustomVerificationEmailTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateReceiptFilterResult createReceiptFilter(CreateReceiptFilterRequest createReceiptFilterRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateReceiptRuleResult createReceiptRule(CreateReceiptRuleRequest createReceiptRuleRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateReceiptRuleSetResult createReceiptRuleSet(CreateReceiptRuleSetRequest createReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public CreateTemplateResult createTemplate(CreateTemplateRequest createTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteConfigurationSetResult deleteConfigurationSet(DeleteConfigurationSetRequest deleteConfigurationSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteConfigurationSetEventDestinationResult deleteConfigurationSetEventDestination(DeleteConfigurationSetEventDestinationRequest deleteConfigurationSetEventDestinationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteConfigurationSetTrackingOptionsResult deleteConfigurationSetTrackingOptions(DeleteConfigurationSetTrackingOptionsRequest deleteConfigurationSetTrackingOptionsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteCustomVerificationEmailTemplateResult deleteCustomVerificationEmailTemplate(DeleteCustomVerificationEmailTemplateRequest deleteCustomVerificationEmailTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteIdentityResult deleteIdentity(DeleteIdentityRequest deleteIdentityRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteIdentityPolicyResult deleteIdentityPolicy(DeleteIdentityPolicyRequest deleteIdentityPolicyRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteReceiptFilterResult deleteReceiptFilter(DeleteReceiptFilterRequest deleteReceiptFilterRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteReceiptRuleResult deleteReceiptRule(DeleteReceiptRuleRequest deleteReceiptRuleRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteReceiptRuleSetResult deleteReceiptRuleSet(DeleteReceiptRuleSetRequest deleteReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteTemplateResult deleteTemplate(DeleteTemplateRequest deleteTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DeleteVerifiedEmailAddressResult deleteVerifiedEmailAddress(DeleteVerifiedEmailAddressRequest deleteVerifiedEmailAddressRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DescribeActiveReceiptRuleSetResult describeActiveReceiptRuleSet(DescribeActiveReceiptRuleSetRequest describeActiveReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DescribeConfigurationSetResult describeConfigurationSet(DescribeConfigurationSetRequest describeConfigurationSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DescribeReceiptRuleResult describeReceiptRule(DescribeReceiptRuleRequest describeReceiptRuleRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public DescribeReceiptRuleSetResult describeReceiptRuleSet(DescribeReceiptRuleSetRequest describeReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        public GetAccountSendingEnabledResult getAccountSendingEnabled() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetAccountSendingEnabledResult getAccountSendingEnabled(GetAccountSendingEnabledRequest getAccountSendingEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetCustomVerificationEmailTemplateResult getCustomVerificationEmailTemplate(GetCustomVerificationEmailTemplateRequest getCustomVerificationEmailTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetIdentityDkimAttributesResult getIdentityDkimAttributes(GetIdentityDkimAttributesRequest getIdentityDkimAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetIdentityMailFromDomainAttributesResult getIdentityMailFromDomainAttributes(GetIdentityMailFromDomainAttributesRequest getIdentityMailFromDomainAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetIdentityNotificationAttributesResult getIdentityNotificationAttributes(GetIdentityNotificationAttributesRequest getIdentityNotificationAttributesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetIdentityPoliciesResult getIdentityPolicies(GetIdentityPoliciesRequest getIdentityPoliciesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetIdentityVerificationAttributesResult getIdentityVerificationAttributes(GetIdentityVerificationAttributesRequest getIdentityVerificationAttributresRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSendQuotaResult getSendQuota() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSendQuotaResult getSendQuota(GetSendQuotaRequest getSendQuotaRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSendStatisticsResult getSendStatistics() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetSendStatisticsResult getSendStatistics(GetSendStatisticsRequest getSendStatisticsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public GetTemplateResult getTemplate(GetTemplateRequest getTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListConfigurationSetsResult listConfigurationSets(ListConfigurationSetsRequest listConfigurationSetsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListCustomVerificationEmailTemplatesResult listCustomVerificationEmailTemplates(ListCustomVerificationEmailTemplatesRequest listCustomVerificationEmailTemplatesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListIdentitiesResult listIdentities() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListIdentitiesResult listIdentities(ListIdentitiesRequest listIdentitiesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListIdentityPoliciesResult listIdentityPolicies(ListIdentityPoliciesRequest listIdentityPoliciesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListReceiptFiltersResult listReceiptFilters(ListReceiptFiltersRequest listReceiptFiltersRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListReceiptRuleSetsResult listReceiptRuleSets(ListReceiptRuleSetsRequest listReceiptRuleSetsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListTemplatesResult listTemplates(ListTemplatesRequest listTemplatesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListVerifiedEmailAddressesResult listVerifiedEmailAddresses() {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ListVerifiedEmailAddressesResult listVerifiedEmailAddresses(ListVerifiedEmailAddressesRequest listVerifiedEmailAddressesRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

		/*
		 * @Override public PutConfigurationSetEventDestinationResult
		 * putConfigurationSetEventDestination(
		 * PutConfigurationSetEventDestinationRequest
		 * putConfigurationSetEventDestinationRequest) { throw new
		 * UnsupportedOperationException("Mock implementation"); }
		 * 
		 * @Override public PutIdentityDkimAttributesResult
		 * putIdentityDkimAttributes(PutIdentityDkimAttributesRequest
		 * putIdentityDkimAttributesRequest) { throw new
		 * UnsupportedOperationException("Mock implementation"); }
		 * 
		 * @Override public PutIdentityMailFromDomainResult
		 * putIdentityMailFromDomain(PutIdentityMailFromDomainRequest
		 * putIdentityMailFromDomainRequest) { throw new
		 * UnsupportedOperationException("Mock implementation"); }
		 * 
		 * @Override public PutIdentityNotificationAttributesResult
		 * putIdentityNotificationAttributes(PutIdentityNotificationAttributesRequest
		 * putIdentityNotificationAttributesRequest) { throw new
		 * UnsupportedOperationException("Mock implementation"); }
		 */
        @Override
        public PutIdentityPolicyResult putIdentityPolicy(PutIdentityPolicyRequest putIdentityPolicyRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public ReorderReceiptRuleSetResult reorderReceiptRuleSet(ReorderReceiptRuleSetRequest reorderReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SendBounceResult sendBounce(SendBounceRequest sendBounceRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SendBulkTemplatedEmailResult sendBulkTemplatedEmail(SendBulkTemplatedEmailRequest sendBulkTemplatedEmailRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SendCustomVerificationEmailResult sendCustomVerificationEmail(SendCustomVerificationEmailRequest sendCustomVerificationEmailRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SendRawEmailResult sendRawEmail(SendRawEmailRequest sendRawEmailRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SendTemplatedEmailResult sendTemplatedEmail(SendTemplatedEmailRequest sendTemplatedEmailRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetActiveReceiptRuleSetResult setActiveReceiptRuleSet(SetActiveReceiptRuleSetRequest setActiveReceiptRuleSetRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetIdentityDkimEnabledResult setIdentityDkimEnabled(SetIdentityDkimEnabledRequest setIdentityDkimEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetIdentityFeedbackForwardingEnabledResult setIdentityFeedbackForwardingEnabled(SetIdentityFeedbackForwardingEnabledRequest setIdentityFeedbackForwardingEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetIdentityHeadersInNotificationsEnabledResult setIdentityHeadersInNotificationsEnabled(SetIdentityHeadersInNotificationsEnabledRequest setIdentityHeadersInNotificationsEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetIdentityNotificationTopicResult setIdentityNotificationTopic(SetIdentityNotificationTopicRequest setIdentityNotificationTopicRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public SetReceiptRulePositionResult setReceiptRulePosition(SetReceiptRulePositionRequest setReceiptRulePositionRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public TestRenderTemplateResult testRenderTemplate(TestRenderTemplateRequest testRenderTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateAccountSendingEnabledResult updateAccountSendingEnabled(UpdateAccountSendingEnabledRequest updateAccountSendingEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateConfigurationSetEventDestinationResult updateConfigurationSetEventDestination(UpdateConfigurationSetEventDestinationRequest updateConfigurationSetEventDestinationRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

		/*
		 * @Override public UpdateConfigurationSetReputationTrackingEnabledResult
		 * updateConfigurationSetReputationTrackingEnabled(
		 * UpdateConfigurationSetReputationTrackingEnabledRequest
		 * updateConfigurationSetReputationTrackingEnabledRequest) { throw new
		 * UnsupportedOperationException("Mock implementation"); }
		 */
        @Override
        public UpdateConfigurationSetSendingEnabledResult updateConfigurationSetSendingEnabled(UpdateConfigurationSetSendingEnabledRequest updateConfigurationSetSendingEnabledRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateConfigurationSetTrackingOptionsResult updateConfigurationSetTrackingOptions(UpdateConfigurationSetTrackingOptionsRequest updateConfigurationSetTrackingOptionsRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateCustomVerificationEmailTemplateResult updateCustomVerificationEmailTemplate(UpdateCustomVerificationEmailTemplateRequest updateCustomVerificationEmailTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateReceiptRuleResult updateReceiptRule(UpdateReceiptRuleRequest updateReceiptRuleRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public UpdateTemplateResult updateTemplate(UpdateTemplateRequest updateTemplateRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public VerifyDomainDkimResult verifyDomainDkim(VerifyDomainDkimRequest verifyDomainDkimRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public VerifyDomainIdentityResult verifyDomainIdentity(VerifyDomainIdentityRequest verifyDomainIdentityRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public VerifyEmailAddressResult verifyEmailAddress(VerifyEmailAddressRequest verifyEmailAddressRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public VerifyEmailIdentityResult verifyEmailIdentity(VerifyEmailIdentityRequest verifyEmailIdentityRequest) {
            throw new UnsupportedOperationException("Mock implementation");
        }

        @Override
        public void shutdown() {
            // No-op for mock
        }

        @Override
        public com.amazonaws.ResponseMetadata getCachedResponseMetadata(com.amazonaws.AmazonWebServiceRequest request) {
            return null;
        }

		@Override
		public PutConfigurationSetDeliveryOptionsResult putConfigurationSetDeliveryOptions(
				PutConfigurationSetDeliveryOptionsRequest putConfigurationSetDeliveryOptionsRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SetIdentityMailFromDomainResult setIdentityMailFromDomain(
				SetIdentityMailFromDomainRequest setIdentityMailFromDomainRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public UpdateConfigurationSetReputationMetricsEnabledResult updateConfigurationSetReputationMetricsEnabled(
				UpdateConfigurationSetReputationMetricsEnabledRequest updateConfigurationSetReputationMetricsEnabledRequest) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AmazonSimpleEmailServiceWaiters waiters() {
			// TODO Auto-generated method stub
			return null;
		}
    }

    @Bean
    public AmazonSNS amazonSNS() {
        return new MockAmazonSNS();
    }

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        return new MockAmazonSES();
    }
}
