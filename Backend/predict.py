from azure.ai.inference import ChatCompletionsClient
from azure.ai.inference.models import SystemMessage, UserMessage
from azure.core.credentials import AzureKeyCredential

endpoint = "https://models.inference.ai.azure.com"
model_name = "meta-llama-3.1-405b-instruct"
token = ""
def predict_result(name, info, net_income, recommendations, holders, dividends, analyst_targets, news, temperature, top_p):
    prompt = f"""
You are a financial analyst. Analyze the following company's financial data and provide insights in a strictly structured format. 

Format your response exactly like this:

Good_Things = ["Sentence 1 describing a good point", "Sentence 2 describing another good point", ...]  
Anomalies = ["Sentence 1 describing an anomaly", "Sentence 2 describing another anomaly", ...]  
Rating = "STRONG BUY" or "BUY" or "HOLD" or "SELL" or "STRONG SELL"  
Price = numeric_value (best price to buy, only the number)

Each point should be a full descriptive sentence. Do not add any extra text or explanation outside this structure.


Company Financial Data:
    Info: {info}, \n
    Net Income: {net_income}, \n
    Recommendations: {recommendations}, \n
    News: {news}, \n
    Holders: {holders}, \n
    Dividends: {dividends}, \n
    Analyst Targets: {analyst_targets}. \n
    
    """
    
    client = ChatCompletionsClient(
        endpoint=endpoint,
        credential=AzureKeyCredential(token),
    )
    response = client.complete(
        messages=[
            #SystemMessage(content="You are a Swing Trader with great Financial Knowledge"),
            SystemMessage(content=""),
            UserMessage(content=prompt),
        ],
        temperature= temperature,
        top_p= top_p,
        max_tokens=4096,
        model=model_name
    )
    print(prompt)
    return response.choices[0].message.content