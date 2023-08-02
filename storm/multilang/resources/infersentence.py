# from transformers import GPT2LMHeadModel, GPT2Tokenizer

import storm

class SplitSentenceBolt(storm.BasicBolt):
    def process(self, tup):
        testSentence = tup.values[0]
        expectedWords = tup.values[1]
        storm.emit([expectedWords, expectedWords])

SplitSentenceBolt().run()

# tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
# model = GPT2LMHeadModel.from_pretrained("gpt2")

# inputs = tokenizer.encode("I enjoy walking with my cute", return_tensors="pt")
# outputs = model.generate(inputs, max_length=10, do_sample=True)

# print(tokenizer.decode(outputs[0]))
