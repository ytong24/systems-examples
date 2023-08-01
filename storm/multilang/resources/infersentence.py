from transformers import GPT2LMHeadModel, GPT2Tokenizer

tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
model = GPT2LMHeadModel.from_pretrained("gpt2")

inputs = tokenizer.encode("I enjoy walking with my cute", return_tensors="pt")
outputs = model.generate(inputs, max_length=10, do_sample=True)

print(tokenizer.decode(outputs[0]))
